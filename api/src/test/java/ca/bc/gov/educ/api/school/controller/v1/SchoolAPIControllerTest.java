package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.SchoolApiResourceApplication;
import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import ca.bc.gov.educ.api.school.repository.SchoolRepository;
import ca.bc.gov.educ.api.school.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type School api controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchoolApiResourceApplication.class})
//@ActiveProfiles("test")
@Slf4j
@SuppressWarnings({"java:S112", "java:S100", "java:S1192", "java:S2699"})
@AutoConfigureMockMvc
public class SchoolAPIControllerTest {

  /**
   * The Controller.
   */
  @Autowired
  SchoolAPIController controller;
  /**
   * The Mock mvc.
   */
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  SchoolRepository schoolRepository;

  private SchoolEntity schoolEntity;
  @Autowired
  SchoolService schoolService;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    schoolEntity = schoolRepository.save(createSchool());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    schoolRepository.deleteAll();
  }

  @Test
  public void testGetSchool_GivenValidMincode_ShouldReturnStatusOK() throws Exception {
    schoolService.reloadCache();
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12345678").with(mockAuthority)).andDo(print()).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.schoolName").value(schoolEntity.getSchoolName()));
  }


  @Test
  public void testGetAllSchool_GivenNoInput_ShouldReturnStatusOK() throws Exception {
    schoolService.reloadCache();
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools").with(mockAuthority)).andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void testGetSchool_GivenNotExistMincode_ShouldReturnStatusNotFound() throws Exception {
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12345670").with(mockAuthority)).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testGetSchool_GivenInvalidMincode_ShouldReturnStatusNotFound() throws Exception {
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12").with(mockAuthority)).andDo(print()).andExpect(status().isNotFound());
  }

  private SchoolEntity createSchool() {
    var mincode = Mincode.builder().distNo("123").schlNo("45678").build();
    return SchoolEntity.builder()
        .mincode(mincode)
        .schoolName("Victoria High School")
        .build();
  }
}
