package ca.bc.gov.educ.api.school.endpoint.v1;

import ca.bc.gov.educ.api.school.struct.v1.FedProvSchoolCode;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import ca.bc.gov.educ.api.school.struct.v1.School;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * The interface School api endpoint.
 */
@RequestMapping("/api/v1/schools")
@OpenAPIDefinition(info = @Info(title = "API to School CRU.", description = "This API is related to school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SCHOOL"})})
public interface SchoolAPIEndpoint {
  /**
   * Get school by mincode.
   *
   * @param mincode the mincode
   * @return the pen request batch
   */
  @GetMapping("/{minCode}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get School Entity.", description = "Endpoint to get School Entity By Mincode.")
  @Schema(name = "School", implementation = School.class)
  School getSchoolByMinCode(@PathVariable("minCode")  String mincode);

  /**
   * Gets all schools.
   *
   * @return the all schools
   */
  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get All School Entity.", description = "Endpoint to get All School Entity.")
  @Schema(name = "School", implementation = School.class)
  List<School> getAllSchools();

  /**
   * Get pen coordinator by mincode.
   *
   * @param mincode the mincode
   * @return the pen request batch
   */
  @GetMapping("/{mincode}/pen-coordinator")
  @PreAuthorize("hasAuthority('SCOPE_READ_PEN_COORDINATOR')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to Pen Coordinator by Mincode.", description = "Endpoint to Pen Coordinator by Mincode.")
  @Schema(name = "PenCoordinator", implementation = PenCoordinator.class)
  ResponseEntity<PenCoordinator> getPenCoordinatorByMinCode(@PathVariable("mincode")  String mincode);

  /**
   * Update pen coordinator by mincode.
   *
   * @param mincode the mincode
   * @return the pen coordinator
   */
  @PutMapping("/{mincode}/pen-coordinator")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_PEN_COORDINATOR')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to Update Pen Coordinator by Mincode.", description = "Endpoint to Update Pen Coordinator by Mincode.")
  @Schema(name = "PenCoordinator", implementation = PenCoordinator.class)
  ResponseEntity<PenCoordinator> updatePenCoordinatorByMinCode(@PathVariable("mincode")  String mincode, @Validated @RequestBody PenCoordinator penCoordinator);

  /**
   * Get pen coordinator by mincode.
   *
   * @return the pen request batch
   */
  @GetMapping("/pen-coordinator")
  @PreAuthorize("hasAuthority('SCOPE_READ_PEN_COORDINATOR')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to Pen Coordinator by Mincode.", description = "Endpoint to Pen Coordinator by Mincode.")
  @Schema(name = "PenCoordinator", implementation = PenCoordinator.class)
  List<PenCoordinator> getPenCoordinators();

  @GetMapping("/federal-province-codes") // implicit to be nom school.
  @PreAuthorize("hasAuthority('SCOPE_READ_FED_PROV_CODE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get All Federal to Provincial mapped data using key.", description = "Endpoint to get All Federal to Provincial mapped data using key.")
  @Schema(name = "FedProvSchoolCodes", implementation = FedProvSchoolCode.class)
  List<FedProvSchoolCode> getFedProvCodes();

  @PostMapping("/federal-province-codes") // implicit to be nom school.
  @PreAuthorize("hasAuthority('SCOPE_WRITE_FED_PROV_CODE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create FedProvSchoolCode.", description = "Endpoint to create FedProvSchoolCode.")
  @Schema(name = "PenCoordinator", implementation = PenCoordinator.class)
  ResponseEntity<FedProvSchoolCode> createFedProvCode(@Validated @RequestBody FedProvSchoolCode fedProvSchoolCode);
}
