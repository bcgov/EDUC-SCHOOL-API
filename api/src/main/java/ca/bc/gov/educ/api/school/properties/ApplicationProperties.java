package ca.bc.gov.educ.api.school.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Class holds all application properties
 *
 * @author Marco Villeneuve
 */
@Component
@Getter
@Setter
public class ApplicationProperties {
  public static final String SCHOOL_API = "SCHOOL-API";
}
