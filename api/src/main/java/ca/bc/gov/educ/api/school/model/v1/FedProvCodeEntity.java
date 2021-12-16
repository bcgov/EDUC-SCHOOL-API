package ca.bc.gov.educ.api.school.model.v1;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type FedProvCode entity.
 *
 */
@Entity
@Table(name = "TABLE_MAP")
@Data
@Immutable
public class FedProvCodeEntity {
  @EmbeddedId
  FedProvCodeId fedProvCodeId;

  @Column(name = "MAPVAL2")
  String provincialCode;
}
