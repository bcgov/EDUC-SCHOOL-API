package ca.bc.gov.educ.api.school.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type School entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolFundingGroup {

  @Size(max = 3)
  @NotNull(message = "distNo can not be null.")
  private String distNo;

  @Size(max = 5)
  @NotNull(message = "schlNo can not be null.")
  private String schlNo;

  private String fundingGroupCode;

  private String fundingGroupSubCode;

  private String formID;

  private String archiveStatus;

  private String createDate;

  private String createTime;

  private String createUsername;

  private String editDate;

  private String editTime;

  private String editUsername;

}
