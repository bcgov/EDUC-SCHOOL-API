package ca.bc.gov.educ.api.school.controller.v1;

import ca.bc.gov.educ.api.school.SchoolApiResourceApplication;
import ca.bc.gov.educ.api.school.mapper.v1.PenCoordinatorMapper;
import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.school.repository.v1.PenCoordinatorRepository;
import ca.bc.gov.educ.api.school.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.school.service.v1.SchoolService;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

  @Autowired
  PenCoordinatorRepository coordinatorRepository;

  private SchoolEntity schoolEntity;
  @Autowired
  SchoolService schoolService;

  /**
   * Sets up.
   */
  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    schoolEntity = schoolRepository.save(createSchool());

    final File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("mock-pen-coordinator.json")).getFile());
    final List<PenCoordinator> structs = new ObjectMapper().readValue(file, new TypeReference<>() {
    });
    this.coordinatorRepository.saveAll(structs.stream().map(PenCoordinatorMapper.mapper::toModel).collect(Collectors.toList()));
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

  @Test
  public void testUpdatePenCoordinator_GivenValidMincode_ShouldReturnStatusOK() throws Exception {
    var mincode = "11253675";
    var penCoordinator = PenCoordinator.builder().mincode(mincode).penCoordinatorName("new coord name").penCoordinatorEmail("new@test.com").build();

    GrantedAuthority grantedAuthority = () -> "WRITE_PEN_COORDINATOR";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(put("/api/v1/schools/{mincode}/pen-coordinator", mincode).with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_PEN_COORDINATOR"))).contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON).content(asJsonString(penCoordinator))).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.penCoordinatorName").value(penCoordinator.getPenCoordinatorName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.penCoordinatorEmail").value(penCoordinator.getPenCoordinatorEmail()));
  }

  @Test
  public void testUpdatePenCoordinator_GivenNotExistMincode_ShouldReturnStatusNotFound() throws Exception {
    var mincode = "12345670";
    var penCoordinator = PenCoordinator.builder().mincode(mincode).penCoordinatorName("new coord name").penCoordinatorEmail("new@test.com").build();

    this.mockMvc.perform(put("/api/v1/schools/{mincode}/pen-coordinator", mincode).with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_PEN_COORDINATOR"))).contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON).content(asJsonString(penCoordinator))).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testUpdatePenCoordinator_GivenInvalidPenCoordinatorEmail_ShouldReturnStatusBadRequest() throws Exception {
    var mincode = "11253675";
    var penCoordinator = PenCoordinator.builder().mincode(mincode).penCoordinatorName("new coord name").penCoordinatorEmail("test.com").build();

    this.mockMvc.perform(put("/api/v1/schools/{mincode}/pen-coordinator", mincode).with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_PEN_COORDINATOR"))).contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON).content(asJsonString(penCoordinator))).andDo(print()).andExpect(status().isBadRequest());
  }

  private SchoolEntity createSchool() {
    var mincode = Mincode.builder().distNo("123").schlNo("45678").build();
    return SchoolEntity.builder()
        .mincode(mincode)
        .schoolName("Victoria High School")
        .build();
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
