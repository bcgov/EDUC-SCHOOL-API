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
  private static final String MINCODE_ATTRIBUTE = "mincode";
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
   * Search for SchoolEntity by Mincode
   *
   * @param mincode the unique mincode for a given school.
   * @return the School entity if found.
   */
  public SchoolEntity retrieveSchoolByMincode(String mincode) {
    Optional<SchoolEntity> result = Optional.empty();
    if(mincode.length() > DISTNO_LENGTH) {
      var distNo = mincode.substring(0, DISTNO_LENGTH);
      var schlNo = mincode.substring(DISTNO_LENGTH);
      result = schoolRepository.findById(Mincode.builder().distNo(distNo).schlNo(schlNo).build());
    }

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, MINCODE_ATTRIBUTE, mincode);
    }
  }
}
