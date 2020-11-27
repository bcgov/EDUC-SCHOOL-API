package ca.bc.gov.educ.api.school.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type School api mvc config.
 *
 * @author Om
 */
@Configuration
public class SchoolAPIMVCConfig implements WebMvcConfigurer {

  /**
   * The Pen reg api interceptor.
   */
  @Getter(AccessLevel.PRIVATE)
  private final SchoolAPIInterceptor schoolAPIInterceptor;

  /**
   * Instantiates a new Pen reg api mvc config.
   *
   * @param schoolAPIInterceptor the pen reg api interceptor
   */
  @Autowired
  public SchoolAPIMVCConfig(final SchoolAPIInterceptor schoolAPIInterceptor) {
    this.schoolAPIInterceptor = schoolAPIInterceptor;
  }

  /**
   * Add interceptors.
   *
   * @param registry the registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(schoolAPIInterceptor).addPathPatterns("/**");
  }
}
