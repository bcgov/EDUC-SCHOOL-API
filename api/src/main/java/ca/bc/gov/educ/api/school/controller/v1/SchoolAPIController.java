package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.endpoint.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.school.mapper.SchoolMapper;
import ca.bc.gov.educ.api.school.service.SchoolService;
import ca.bc.gov.educ.api.school.struct.v1.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * The type School api controller.
 */
@RestController
@EnableResourceServer
@Slf4j
public class SchoolAPIController implements SchoolAPIEndpoint {
  @Getter(AccessLevel.PRIVATE)
  private final SchoolService service;

  /**
   * The constant mapper.
   */
  private static final SchoolMapper mapper = SchoolMapper.mapper;

  /**
   * Instantiates a new School api controller.
   *
   * @param service the service
   */
  @Autowired
  public SchoolAPIController(SchoolService service) {
    this.service = service;
  }

  @Override
  public School getSchoolByMinCode(String mincode) {
    return mapper.toStructure(getService().retrieveSchoolByMincode(mincode));
  }

  @Override
  public List<School> getAllSchools() {
    return getService().retrieveAllSchools().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

}
