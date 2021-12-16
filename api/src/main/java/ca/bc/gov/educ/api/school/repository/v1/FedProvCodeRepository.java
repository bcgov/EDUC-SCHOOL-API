package ca.bc.gov.educ.api.school.repository.v1;

import ca.bc.gov.educ.api.school.model.v1.FedProvCodeEntity;
import ca.bc.gov.educ.api.school.model.v1.FedProvCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FedProvCodeRepository extends JpaRepository<FedProvCodeEntity, FedProvCodeId> {
  List<FedProvCodeEntity> findByFedProvCodeIdKey(String mappingKey);
}
