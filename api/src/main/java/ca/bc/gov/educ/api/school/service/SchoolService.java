package ca.bc.gov.educ.api.school.service;

import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import ca.bc.gov.educ.api.school.repository.SchoolRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
public class SchoolService {
  private final Executor cacheLoadingExecutor = Executors.newSingleThreadExecutor();
  private static final String MINCODE_ATTRIBUTE = "mincode";
  private static final int DIST_NO_LENGTH = 3;
  private Map<Mincode, SchoolEntity> MIN_CODE_SCHOOL_MAP;
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
    Optional<SchoolEntity> result = Optional.empty();
    if (mincode.length() > DIST_NO_LENGTH) {
      var distNo = mincode.substring(0, DIST_NO_LENGTH);
      var schoolNumber = mincode.substring(DIST_NO_LENGTH);
      result = Optional.ofNullable(this.MIN_CODE_SCHOOL_MAP.get(Mincode.builder().distNo(distNo).schlNo(schoolNumber).build()));
    }

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, MINCODE_ATTRIBUTE, mincode);
    }
  }

  @PostConstruct
  public void init() {
    cacheLoadingExecutor.execute(this::setSchoolData);
  }

  private void setSchoolData() {
    Lock writeLock = minCodeSchoolMapLock.writeLock();
    try {
      writeLock.lock();
      this.MIN_CODE_SCHOOL_MAP = schoolRepository.findAll().stream().collect(Collectors.toConcurrentMap(SchoolEntity::getMincode, Function.identity()));
      log.info("loaded {} entries into min code school map", MIN_CODE_SCHOOL_MAP.values().size());
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
