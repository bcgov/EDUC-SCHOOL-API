package ca.bc.gov.educ.api.school.repository.v1;

import ca.bc.gov.educ.api.school.model.v1.Mincode;
import ca.bc.gov.educ.api.school.model.v1.PenCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenCoordinatorRepository extends JpaRepository<PenCoordinatorEntity, Mincode> {
}
