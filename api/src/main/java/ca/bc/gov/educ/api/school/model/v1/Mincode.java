package ca.bc.gov.educ.api.school.model.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Mincode.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Mincode implements Serializable {
  /**
   * The Dist no.
   */
  @Column(name = "DISTNO", nullable = false, length = 3)
  protected String distNo;

  /**
   * The Schl no.
   */
  @Column(name = "SCHLNO", nullable = false, length = 5)
  protected String schlNo;
}
