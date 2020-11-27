package ca.bc.gov.educ.api.school.endpoint;

import ca.bc.gov.educ.api.school.struct.v1.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


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
   * @param minCode the minCode
   * @return the pen request batch
   */
  @GetMapping
  @PreAuthorize("#oauth2.hasScope('READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get School Entity.", description = "Endpoint to get School Entity By Mincode.")
  @Schema(name = "School", implementation = School.class)
  School getSchoolByMinCode(@Param("minCode")  String minCode);
}
