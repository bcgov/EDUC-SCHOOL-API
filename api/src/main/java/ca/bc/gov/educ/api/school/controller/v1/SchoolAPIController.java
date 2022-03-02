package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.mapper.v1.FedProvCodeMapper;
import ca.bc.gov.educ.api.school.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.school.service.v1.FedProvCodeService;
import ca.bc.gov.educ.api.school.service.v1.PenCoordinatorService;
import ca.bc.gov.educ.api.school.service.v1.SchoolService;
import ca.bc.gov.educ.api.school.struct.v1.FedProvSchoolCode;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import ca.bc.gov.educ.api.school.struct.v1.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * The type School api controller.
 */
@RestController
@Slf4j
public class SchoolAPIController implements SchoolAPIEndpoint {
  /**
   * The constant mapper.
   */
  private static final SchoolMapper mapper = SchoolMapper.mapper;
  private static final FedProvCodeMapper fedProvCodeMapper = FedProvCodeMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final SchoolService service;
  private final PenCoordinatorService penCoordinatorService;
  private final FedProvCodeService fedProvCodeService;

  /**
   * Instantiates a new School api controller.
   *
   * @param service               the service
   * @param penCoordinatorService the pen coordinator service
   */
  @Autowired
  public SchoolAPIController(SchoolService service, PenCoordinatorService penCoordinatorService, FedProvCodeService fedProvCodeService) {
    this.service = service;
    this.penCoordinatorService = penCoordinatorService;
    this.fedProvCodeService = fedProvCodeService;
  }

  @Override
  public School getSchoolByMinCode(String mincode) {
    return mapper.toStructure(getService().retrieveSchoolByMincode(mincode));
  }

  @Override
  public List<School> getAllSchools() {
    return getService().retrieveAllSchoolStructs();
  }

  @Override
  public ResponseEntity<PenCoordinator> getPenCoordinatorByMinCode(String mincode) {
    return ResponseEntity.ok(this.penCoordinatorService.getPenCoordinatorByMinCode(mincode).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public ResponseEntity<PenCoordinator> updatePenCoordinatorByMinCode(String mincode, PenCoordinator penCoordinator) {
    return ResponseEntity.ok(this.penCoordinatorService.updatePenCoordinatorByMinCode(mincode, penCoordinator).orElseThrow(EntityNotFoundException::new));
  }

  @Override
  public List<PenCoordinator> getPenCoordinators() {
    return this.penCoordinatorService.getPenCoordinators();
  }

  @Override
  public List<FedProvSchoolCode> getFedProvCodes() {
    return this.fedProvCodeService.getFedProvCodes("NOM_SCHL").stream().filter(Objects::nonNull).map(fedProvCodeMapper::toStruct).collect(Collectors.toList());
  }

  @Override
  public ResponseEntity<FedProvSchoolCode> createFedProvCode(FedProvSchoolCode fedProvSchoolCode) {
    return ResponseEntity.ok(this.fedProvCodeService.createFedProvCode(fedProvSchoolCode));
  }
  @Override
  @Transactional
  public ResponseEntity<Void> deleteFedProvCode(FedProvSchoolCode fedProvSchoolCode) {
    this.fedProvCodeService.deleteFedProvCode(fedProvSchoolCode);
    return ResponseEntity.noContent().build();
  }
}
