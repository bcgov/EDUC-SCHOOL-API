package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.school.service.v1.PenCoordinatorService;
import ca.bc.gov.educ.api.school.service.v1.SchoolService;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import ca.bc.gov.educ.api.school.struct.v1.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
  @Getter(AccessLevel.PRIVATE)
  private final SchoolService service;
  private final PenCoordinatorService penCoordinatorService;

  /**
   * Instantiates a new School api controller.
   *
   * @param service               the service
   * @param penCoordinatorService the pen coordinator service
   */
  @Autowired
  public SchoolAPIController(SchoolService service, PenCoordinatorService penCoordinatorService) {
    this.service = service;
    this.penCoordinatorService = penCoordinatorService;
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
  public List<PenCoordinator> getPenCoordinators() {
    return this.penCoordinatorService.getPenCoordinators();
  }

}
