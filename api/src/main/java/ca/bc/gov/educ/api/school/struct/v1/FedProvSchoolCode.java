package ca.bc.gov.educ.api.school.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedProvSchoolCode {
  @NotNull
  @Pattern(regexp = "NOM_SCHL", message = "Only support nom school")
  String key;
  @NotNull
  String federalCode;
  @Size(max = 8)
  @NotNull
  String provincialCode;
}
