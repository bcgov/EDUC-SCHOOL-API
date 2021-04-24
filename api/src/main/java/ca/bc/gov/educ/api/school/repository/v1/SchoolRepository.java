package ca.bc.gov.educ.api.school.repository.v1;

import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Pen name text repository.
 */
@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Mincode> {
}
