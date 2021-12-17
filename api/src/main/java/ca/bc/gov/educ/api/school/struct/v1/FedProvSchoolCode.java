package ca.bc.gov.educ.api.school.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedProvSchoolCode {
  @Pattern(regexp = "NOM_SCHL", message = "Only support nom school")
  String key;
  String federalCode;
  @Size(max = 8)
  String provincialCode;
}
