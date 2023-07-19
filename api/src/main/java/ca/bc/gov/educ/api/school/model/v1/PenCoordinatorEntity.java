package ca.bc.gov.educ.api.school.model.v1;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "PEN_COORDINATOR")
@Data
@Immutable
public class PenCoordinatorEntity {

  @EmbeddedId
  Mincode mincode;

  @Column(name = "PEN_COORDINATOR_NAME")
  String penCoordinatorName;
  @Column(name = "PEN_COORDINATOR_EMAIL")
  String penCoordinatorEmail;
  @Column(name = "PEN_COORDINATOR_FAX")
  String penCoordinatorFax;
  @Column(name = "SEND_PEN_RESULTS_VIA")
  String sendPenResultsVia;
}
