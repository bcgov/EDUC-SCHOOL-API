package ca.bc.gov.educ.api.school.service;

import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.exception.SchoolAPIRuntimeException;
import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import ca.bc.gov.educ.api.school.repository.SchoolRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * The type School service.
 */
@Service
@Slf4j
public class SchoolService {
  private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
  private static final String MINCODE_ATTRIBUTE = "mincode";
  private static final int DIST_NO_LENGTH = 3;
  private Map<Mincode, SchoolEntity> mincodeSchoolEntityMap;
  private final ReadWriteLock minCodeSchoolMapLock = new ReentrantReadWriteLock();

  /**
   * The School repository.
   */
  @Getter(PRIVATE)
  private final SchoolRepository schoolRepository;

  /**
   * Instantiates a new School service.
   *
   * @param schoolRepository School repository
   */
  @Autowired
  public SchoolService(SchoolRepository schoolRepository) {
    this.schoolRepository = schoolRepository;
  }

  /**
   * Search for SchoolEntity by Mincode
   *
   * @param mincode the unique mincode for a given school.
   * @return the School entity if found.
   */
  public SchoolEntity retrieveSchoolByMincode(String mincode) {
    try {
      Optional<SchoolEntity> result = Optional.empty();
      if (mincode.length() > DIST_NO_LENGTH) {
        var distNo = mincode.substring(0, DIST_NO_LENGTH);
        var schoolNumber = mincode.substring(DIST_NO_LENGTH);
        final Callable<SchoolEntity> callableSchool = () -> this.mincodeSchoolEntityMap.get(Mincode.builder().distNo(distNo).schlNo(schoolNumber).build());
        result = Optional.ofNullable(singleThreadExecutor.submit(callableSchool).get());
      }

      if (result.isPresent()) {
        return result.get();
      } else {
        throw new EntityNotFoundException(SchoolEntity.class, MINCODE_ATTRIBUTE, mincode);
      }
    } catch (final ExecutionException | InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new SchoolAPIRuntimeException(exception.getMessage());
    }

  }

  /**
   * Retrieve all schools list.
   *
   * @return the list
   */
  public List<SchoolEntity> retrieveAllSchools() {
    try {
      final Callable<List<SchoolEntity>> listCallableSchool = () -> new ArrayList<>(this.mincodeSchoolEntityMap.values());
      return singleThreadExecutor.submit(listCallableSchool).get();
    } catch (final Exception exception) {
      throw new SchoolAPIRuntimeException(exception.getMessage());
    }
  }

  /**
   * Init.
   */
  @PostConstruct
  public void init() {
    singleThreadExecutor.execute(this::setSchoolData);
  }

  private void setSchoolData() {
    Lock writeLock = minCodeSchoolMapLock.writeLock();
    try {
      writeLock.lock();
      this.mincodeSchoolEntityMap = schoolRepository.findAll().stream().collect(Collectors.toConcurrentMap(SchoolEntity::getMincode, Function.identity()));
      log.info("loaded {} entries into min code school map", mincodeSchoolEntityMap.values().size());
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Reload cache at midnight
   */
  @Scheduled(cron = "0 0 0 * * *")
  @Retryable
  public void reloadCache() {
    log.info("started reloading cache..");
    this.setSchoolData();
    log.info("reloading cache completed..");
  }

}
