package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.school.exception.errors.ApiError;
import ca.bc.gov.educ.api.school.mapper.LocalDateTimeMapper;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


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
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

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
    validateFedProvCodePayload(fedProvSchoolCode);
    return ResponseEntity.ok(this.fedProvCodeService.createFedProvCode(fedProvSchoolCode));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteFedProvCode(FedProvSchoolCode fedProvSchoolCode) {
    this.fedProvCodeService.deleteFedProvCode(fedProvSchoolCode);
    return ResponseEntity.noContent().build();
  }

  private void validateFedProvCodePayload(FedProvSchoolCode fedProvSchoolCode) {
    try {
      var school = getSchoolByMinCode(fedProvSchoolCode.getProvincialCode());
      if ((school.getClosedDate() != null && LocalDate.parse(school.getClosedDate(), formatter).isBefore(LocalDate.now()))) {
        ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid provincial code provided, school is closed.").status(BAD_REQUEST).build();
        throw new InvalidPayloadException(error);
      }
    }catch (EntityNotFoundException e) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid provincial code provided.").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }
    var fedProvCodeMap = this.getFedProvCodes();
    if (fedProvCodeMap.stream().anyMatch(fedProvSchoolCode1 -> fedProvSchoolCode1.getProvincialCode().equals(fedProvSchoolCode.getProvincialCode())
      && fedProvSchoolCode1.getKey().equals(fedProvSchoolCode.getKey())
      && fedProvSchoolCode1.getFederalCode().equals(fedProvSchoolCode.getFederalCode()))) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Mapping already exists.").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }
  }
}
