package ca.bc.gov.educ.api.school.service.v1;

import ca.bc.gov.educ.api.school.mapper.v1.PenCoordinatorMapper;
import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.PenCoordinatorEntity;
import ca.bc.gov.educ.api.school.repository.v1.PenCoordinatorRepository;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PenCoordinatorService {
  private final Executor bgTaskExecutor = new EnhancedQueueExecutor.Builder()
      .setCorePoolSize(1).setMaximumPoolSize(2).setKeepAliveTime(Duration.ofSeconds(60)).build();
  private final PenCoordinatorRepository penCoordinatorRepository;
  private final ReadWriteLock penCoordinatorMapLock = new ReentrantReadWriteLock();
  @Setter
  private Map<Mincode, PenCoordinatorEntity> penCoordinatorMap;

  @Value("${initialization.background.enabled}")
  private Boolean isBackgroundInitializationEnabled;

  @Autowired
  public PenCoordinatorService(final PenCoordinatorRepository penCoordinatorRepository) {
    this.penCoordinatorRepository = penCoordinatorRepository;
  }

  @PostConstruct
  public void init() {
    this.loadPenCoordinatorDataIntoMemory();
  }

  private void loadPenCoordinatorDataIntoMemory() {
    if (this.isBackgroundInitializationEnabled != null && this.isBackgroundInitializationEnabled) {
      this.bgTaskExecutor.execute(this::populatePenCoordinatorMap);
    } else {
      this.populatePenCoordinatorMap();
    }
  }

  private void populatePenCoordinatorMap() {
    this.penCoordinatorMap = this.penCoordinatorRepository.findAll().stream().collect(Collectors.toConcurrentMap(PenCoordinatorEntity::getMincode, Function.identity()));
  }

  @Scheduled(cron = "${schedule.jobs.load.pen.coordinators.cron}") // 0 0/15 * * * * every 15 minutes
  public void scheduled() {
    final Lock writeLock = this.penCoordinatorMapLock.writeLock();
    try {
      writeLock.lock();
      this.loadPenCoordinatorDataIntoMemory();
    } finally {
      writeLock.unlock();
    }
  }

  public Optional<PenCoordinator> getPenCoordinatorByMinCode(final Mincode mincode) {
    return Optional.ofNullable(PenCoordinatorMapper.mapper.toStruct(this.penCoordinatorMap.get(mincode)));
  }

  public Optional<PenCoordinator> getPenCoordinatorByMinCode(final String mincode) {
    if (StringUtils.length(mincode) != 8 || !StringUtils.isNumeric(mincode)) {
      return Optional.empty();
    }
    val ministryCode = Mincode.builder().distNo(mincode.substring(0, 3)).schlNo(mincode.substring(3, 8)).build();
    return Optional.ofNullable(PenCoordinatorMapper.mapper.toStruct(this.penCoordinatorMap.get(ministryCode)));
  }
}
