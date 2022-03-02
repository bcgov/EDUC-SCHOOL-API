package ca.bc.gov.educ.api.school.service.v1;

import ca.bc.gov.educ.api.school.model.v1.FedProvCodeEntity;
import ca.bc.gov.educ.api.school.repository.v1.FedProvCodeRepository;
import ca.bc.gov.educ.api.school.struct.v1.FedProvSchoolCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * The type FedProvCode service.
 */
@Service
@Slf4j
public class FedProvCodeService extends BaseService {
  /**
   * The FedProvCode repository.
   */
  @Getter(PRIVATE)
  private final FedProvCodeRepository fedProvCodeRepository;

  /**
   * Instantiates a new FedProvCode service.
   *
   * @param fedProvCodeRepository fedProvCode repository
   * @param emf entity manager factory
   */
  @Autowired
  public FedProvCodeService(final FedProvCodeRepository fedProvCodeRepository, final EntityManagerFactory emf) {
    super(emf);
    this.fedProvCodeRepository = fedProvCodeRepository;
  }

  public List<FedProvCodeEntity> getFedProvCodes(final String mappingKey) {
    return this.fedProvCodeRepository.findByFedProvCodeIdKey(mappingKey);
  }

  public FedProvSchoolCode createFedProvCode(final FedProvSchoolCode fedProvCode) {
    val updateStatement = this.prepareUpdateStatement(fedProvCode);
    this.execUpdate(updateStatement);
    return fedProvCode;
  }

  protected String prepareUpdateStatement(final FedProvSchoolCode fedProvCode) {
    val builder = new StringBuilder();
    builder
      .append("INSERT INTO TABLE_MAP (TABMAP_KEY, MAPVAL1, MAPVAL2) VALUES ('") // end with beginning single quote
      .append(StringUtils.trimToEmpty(fedProvCode.getKey()))
      .append("'") // end single quote
      .append(", '") // end with beginning single quote
      .append(StringUtils.trimToEmpty(fedProvCode.getFederalCode()))
      .append("'") // end single quote
      .append(", '") // end with beginning single quote
      .append(StringUtils.trimToEmpty(fedProvCode.getProvincialCode()))
      .append("')"); // end single quote
    return builder.toString();
  }

  protected String prepareDeleteStatement(final FedProvSchoolCode fedProvCode) {
    val builder = new StringBuilder();
    builder
            .append("DELETE FROM TABLE_MAP WHERE MAPVAL2 = '") // end with beginning single quote
            .append(StringUtils.trimToEmpty(fedProvCode.getProvincialCode()))
                            .append("' AND TABMAP_KEY = '")
                            .append(StringUtils.trimToEmpty(fedProvCode.getKey()))
                            .append("' AND MAPVAL1 = '")
                            .append(StringUtils.trimToEmpty(fedProvCode.getFederalCode()))
.append("'");

    // end single quote
    return builder.toString();
  }

  public void deleteFedProvCode(FedProvSchoolCode fedProvSchoolCode) {
    val deleteStatement = this.prepareDeleteStatement(fedProvSchoolCode);
    this.execUpdate(deleteStatement);
  }
}
