package ca.bc.gov.educ.api.school.struct.v1;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
public class FedProvSchoolCode {
  @Pattern(regexp = "NOM_SCHL", message = "Only support nom school")
  String key;
  String federalCode;
  @Size(max = 8)
  String provincialCode;
}
