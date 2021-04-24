package ca.bc.gov.educ.api.school.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenCoordinator {
  String mincode;
  String districtNumber;
  String schoolNumber;
  String penCoordinatorName;
  String penCoordinatorEmail;
  String penCoordinatorFax;
  String sendPenResultsVia;
}
