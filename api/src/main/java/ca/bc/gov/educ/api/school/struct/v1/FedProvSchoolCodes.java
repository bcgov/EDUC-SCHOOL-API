package ca.bc.gov.educ.api.school.struct.v1;

import lombok.Data;

@Data
public class FedProvSchoolCodes {
  String key;
  String federalCode;
  String provincialCode;
}
