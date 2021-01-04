package ca.bc.gov.educ.api.school.repository;

import ca.bc.gov.educ.api.school.model.Mincode;
import ca.bc.gov.educ.api.school.model.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Pen name text repository.
 */
@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Mincode> {
}
