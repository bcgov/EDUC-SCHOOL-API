package ca.bc.gov.educ.api.school.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Min Code.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MinCode implements Serializable {
  @Column(name = "DISTNO", nullable = false, length = 3)
  protected String distNo;

  @Column(name = "SCHLNO", nullable = false, length = 5)
  protected String schlNo;
}
