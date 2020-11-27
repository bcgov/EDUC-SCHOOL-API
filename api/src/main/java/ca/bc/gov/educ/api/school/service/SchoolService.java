package ca.bc.gov.educ.api.school.service;

import ca.bc.gov.educ.api.school.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.school.model.*;
import ca.bc.gov.educ.api.school.repository.SchoolRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
public class SchoolService {
  private static final String MINCODE_ATTRIBUTE = "minCode";
  private static final int DISTNO_LENGTH = 3;

  /**
   * The School repository.
   */
  @Getter(PRIVATE)
  private final SchoolRepository schoolRepository;

  /**
   * Instantiates a new School service.
   *
   * @param schoolRepository  School repository
   */
  @Autowired
  public SchoolService(SchoolRepository schoolRepository) {
    this.schoolRepository = schoolRepository;
  }

  /**
   * Search for SchoolEntity by MinCode
   *
   * @param minCode the unique minCode for a given school.
   * @return the School entity if found.
   */
  public SchoolEntity retrieveSchoolByMinCode(String minCode) {
    Optional<SchoolEntity> result = Optional.empty();
    if(minCode.length() > DISTNO_LENGTH) {
      var distNo = minCode.substring(0, DISTNO_LENGTH);
      var schlNo = minCode.substring(DISTNO_LENGTH);
      result = schoolRepository.findById(MinCode.builder().distNo(distNo).schlNo(schlNo).build());
    }

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, MINCODE_ATTRIBUTE, minCode);
    }
  }
}
