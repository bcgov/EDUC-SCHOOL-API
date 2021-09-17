package ca.bc.gov.educ.api.school.model.v1;

/**
 * This is a projection interface used to get the native query results.
 * this is used in this repository method.
 * {@link ca.bc.gov.educ.api.school.repository.v1.SchoolRepository#findAllFedAndProvSchoolCodesByMappingKey(String)}
 * The implementation class will be delivered by Spring boot magically.
 * for more information look at the below class.
 * {@link org.springframework.data.projection.ProjectionFactory}
 *
 * @author om
 */
public interface FedProvCode {
  String getKey();

  String getFederalCode();

  String getProvincialCode();
}
