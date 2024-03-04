package ca.bc.gov.educ.api.school.model.v1;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SchoolFundingGroupID implements Serializable {

  @Column(name = "DISTNO", nullable = false, length = 3)
  protected String distNo;

  @Column(name = "SCHLNO", nullable = false, length = 5)
  protected String schlNo;

  @Column(name = "FUNDING_GROUP_CODE")
  private String fundingGroupCode;

  @Column(name = "FUNDING_GROUP_SUBCODE")
  private String fundingGroupSubCode;
}
