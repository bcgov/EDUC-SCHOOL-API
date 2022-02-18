package ca.bc.gov.educ.api.school.service.v1;

import ca.bc.gov.educ.api.school.mapper.v1.PenCoordinatorMapper;
import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.PenCoordinatorEntity;
import ca.bc.gov.educ.api.school.repository.v1.PenCoordinatorRepository;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PenCoordinatorServiceTest {
  @Autowired
  PenCoordinatorRepository coordinatorRepository;

  @Autowired
  PenCoordinatorService service;

  @Before
  public void setup() throws IOException {
    final File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("mock-pen-coordinator.json")).getFile());
    final List<PenCoordinator> structs = new ObjectMapper().readValue(file, new TypeReference<>() {
    });
    this.coordinatorRepository.saveAll(structs.stream().map(PenCoordinatorMapper.mapper::toModel).collect(Collectors.toList()));
  }

  @Test
  public void testGetPenCoordinators_shouldProduceOutput() {
    val data = this.service.getPenCoordinators();
    for(val coordinator : data) {
      assertThat(coordinator.getPenCoordinatorName()).isNotNull();
    }
  }

  @Test
  public void testGetPenCoordinator_givenDifferentInputs_shouldProduceOutput() {
    val data = this.service.getPenCoordinatorByMinCode("123546789");
    assertThat(data).isEmpty();
    val dataOptional = this.service.getPenCoordinatorByMinCode("19337120");
    assertThat(dataOptional).isPresent();
  }


  @Test
  public void testGetPenCoordinatorEmail_givenDifferentInputsOfMincodeObject_shouldProduceOutput() {
    val data = this.service.getPenCoordinatorByMinCode(Mincode.builder().distNo("123").schlNo("45678").build());
    assertThat(data).isEmpty();
    val dataOptional = this.service.getPenCoordinatorByMinCode(Mincode.builder().distNo("193").schlNo("37120").build());
    assertThat(dataOptional).isPresent();
  }
}
