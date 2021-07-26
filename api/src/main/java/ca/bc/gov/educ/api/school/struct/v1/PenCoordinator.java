package ca.bc.gov.educ.api.school.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenCoordinator {
  String mincode;
  String districtNumber;
  String schoolNumber;
  @Size(max = 40)
  @NotBlank(message = "Legal Last Name can not be blank.")
  String penCoordinatorName;
  @Size(max = 100)
  @Email(message = "Email must be valid email address.")
  String penCoordinatorEmail;
  String penCoordinatorFax;
  String sendPenResultsVia;
}
