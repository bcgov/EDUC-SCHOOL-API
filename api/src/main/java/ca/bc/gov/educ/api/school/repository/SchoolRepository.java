package ca.bc.gov.educ.api.school.repository;

import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Pen name text repository.
 */
@Repository
public interface SchoolRepository extends CrudRepository<SchoolEntity, Mincode> {
}
