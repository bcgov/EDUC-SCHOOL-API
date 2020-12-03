package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.SchoolApiResourceApplication;
import ca.bc.gov.educ.api.school.exception.RestExceptionHandler;
import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import ca.bc.gov.educ.api.school.repository.SchoolRepository;
import ca.bc.gov.educ.api.school.support.WithMockOAuth2Scope;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type School api controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SchoolApiResourceApplication.class})
//@ActiveProfiles("test")
@Slf4j
@SuppressWarnings({"java:S112", "java:S100", "java:S1192","java:S2699"})
public class SchoolAPIControllerTest {

  /**
   * The Controller.
   */
  @Autowired
  SchoolAPIController controller;
  /**
   * The Mock mvc.
   */
  private MockMvc mockMvc;

  @Autowired
  SchoolRepository schoolRepository;

  private SchoolEntity schoolEntity;

  /**
   * Sets up.
   *
   * @throws IOException the io exception
   */
  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
      .setControllerAdvice(new RestExceptionHandler()).build();
    schoolEntity = schoolRepository.save(createSchool("123", "45678"));
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    schoolRepository.deleteAll();
  }

  @Test
  @WithMockOAuth2Scope(scope = "READ_SCHOOL")
  public void testGetSchool_GivenValidMincode_ShouldReturnStatusOK() throws Exception {
    this.mockMvc.perform(get("/api/v1/schools?mincode=12345678")).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.schoolName").value(schoolEntity.getSchoolName()));
  }

  @Test
  @WithMockOAuth2Scope(scope = "READ_SCHOOL")
  public void testGetSchool_GivenNotExistMincode_ShouldReturnStatusNotFound() throws Exception {
    this.mockMvc.perform(get("/api/v1/schools?mincode=12345670")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  @WithMockOAuth2Scope(scope = "READ_SCHOOL")
  public void testGetSchool_GivenInvalidMincode_ShouldReturnStatusNotFound() throws Exception {
    this.mockMvc.perform(get("/api/v1/schools?mincode=12")).andDo(print()).andExpect(status().isNotFound());
  }

  private SchoolEntity createSchool(String distNo, String schlNo) {
    var mincode = Mincode.builder().distNo(distNo).schlNo(schlNo).build();
    var school = SchoolEntity.builder()
      .mincode(mincode)
      .schoolName("Victoria High School")
      .build();
    return school;
  }
}
