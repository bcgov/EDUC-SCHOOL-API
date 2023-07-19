package ca.bc.gov.educ.api.school.service.v1;

import ca.bc.gov.educ.api.school.mapper.v1.PenCoordinatorMapper;
import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.repository.v1.PenCoordinatorRepository;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PenCoordinatorService extends BaseService {
  private final PenCoordinatorRepository penCoordinatorRepository;

  @Autowired
  public PenCoordinatorService(final PenCoordinatorRepository penCoordinatorRepository, final EntityManagerFactory emf) {
    super(emf);
    this.penCoordinatorRepository = penCoordinatorRepository;
  }

  public Optional<PenCoordinator> getPenCoordinatorByMinCode(final Mincode mincode) {
    return this.penCoordinatorRepository.findById(mincode).map(PenCoordinatorMapper.mapper::toStruct);
  }

  public Optional<PenCoordinator> getPenCoordinatorByMinCode(final String mincode) {
    if (StringUtils.length(mincode) != 8 || !StringUtils.isNumeric(mincode)) {
      return Optional.empty();
    }
    val ministryCode = Mincode.builder().distNo(mincode.substring(0, 3)).schlNo(mincode.substring(3, 8)).build();
    return this.getPenCoordinatorByMinCode(ministryCode);
  }

  public Optional<PenCoordinator> updatePenCoordinatorByMinCode(final String mincode, final PenCoordinator penCoordinator) {
    if (StringUtils.length(mincode) != 8 || !StringUtils.isNumeric(mincode)) {
      return Optional.empty();
    }
    val ministryCode = Mincode.builder().distNo(mincode.substring(0, 3)).schlNo(mincode.substring(3, 8)).build();
    val updateStatement = this.prepareUpdateStatement(ministryCode, penCoordinator);
    val count = this.execUpdate(updateStatement);
    if (count > 0) {
      return this.getPenCoordinatorByMinCode(ministryCode);
    } else {
      return Optional.empty();
    }
  }

  public List<PenCoordinator> getPenCoordinators() {
    return this.penCoordinatorRepository.findAll().stream().map(PenCoordinatorMapper.mapper::toStruct).toList();
  }

  protected String prepareUpdateStatement(final Mincode mincode, final PenCoordinator penCoordinator) {
    val builder = new StringBuilder();
    builder
      .append("UPDATE PEN_COORDINATOR SET PEN_COORDINATOR_NAME='") // end with beginning single quote
      .append(StringUtils.trimToEmpty(penCoordinator.getPenCoordinatorName()))
      .append("'") // end single quote
      .append(", PEN_COORDINATOR_EMAIL='") // end with beginning single quote
      .append(StringUtils.trimToEmpty(penCoordinator.getPenCoordinatorEmail()))
      .append("'") // end single quote
      .append(" WHERE ") // starts and ends with a space for valid sql statement
      .append("DISTNO='") // end with beginning single quote
      .append(mincode.getDistNo())
      .append("'") // end single quote
      .append(" AND SCHLNO='") // end with beginning single quote
      .append(mincode.getSchlNo())
      .append("'"); // end single quote
    return builder.toString();
  }
}
