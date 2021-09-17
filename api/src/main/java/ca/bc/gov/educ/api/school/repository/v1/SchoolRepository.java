package ca.bc.gov.educ.api.school.repository.v1;

import ca.bc.gov.educ.api.school.model.v1.FedProvCode;
import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Pen name text repository.
 */
@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Mincode> {

  @Query(value = "SELECT tableMap.TABMAP_KEY as key, MAPVAL1 as federalCode, MAPVAL2 as provincialCode FROM TABLE_MAP tableMap WHERE tableMap.TABMAP_KEY = :mappingKey", nativeQuery = true)
  List<FedProvCode> findAllFedAndProvSchoolCodesByMappingKey(String mappingKey);
}
