package ca.bc.gov.educ.api.school.model.v1;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
