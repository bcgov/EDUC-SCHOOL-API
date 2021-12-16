package ca.bc.gov.educ.api.school.model.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type FedProvCode id.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FedProvCodeId implements Serializable {
  @Column(name = "TABMAP_KEY")
  protected String key;

  @Column(name = "MAPVAL1")
  protected String federalCode;
}
