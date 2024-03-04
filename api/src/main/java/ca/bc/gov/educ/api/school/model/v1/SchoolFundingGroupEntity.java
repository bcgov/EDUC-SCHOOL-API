package ca.bc.gov.educ.api.school.model.v1;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "SCHOOL_FUNDING_MASTER")
public class SchoolFundingGroupEntity {
  @EmbeddedId
  private SchoolFundingGroupID schoolFundingGroupID;

  @Column(name = "FORM_ID")
  private String formID;

  @Column(name = "ARCHIVE_STATUS")
  private String archiveStatus;

  @Column(name = "CREATE_DATE")
  private String createDate;

  @Column(name = "CREATE_TIME")
  private String createTime;

  @Column(name = "CREATE_USERNAME")
  private String createUsername;

  @Column(name = "EDIT_DATE")
  private String editDate;

  @Column(name = "EDIT_TIME")
  private String editTime;

  @Column(name = "EDIT_USERNAME")
  private String editUsername;

}
