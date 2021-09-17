package ca.bc.gov.educ.api.school.mapper.v1;

import ca.bc.gov.educ.api.school.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.school.mapper.StringMapper;
import ca.bc.gov.educ.api.school.model.v1.FedProvCode;
import ca.bc.gov.educ.api.school.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.school.struct.v1.FedProvSchoolCodes;
import ca.bc.gov.educ.api.school.struct.v1.School;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface School mapper.
 */
@Mapper(uses = {LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMapper {

  /**
   * The constant mapper.
   */
  SchoolMapper mapper = Mappers.getMapper(SchoolMapper.class);

  /**
   * To structure school.
   *
   * @param schoolEntity the school entity
   * @return the school
   */
  @Mapping(target = "distNo", source = "schoolEntity.mincode.distNo")
  @Mapping(target = "schlNo", source = "schoolEntity.mincode.schlNo")
  School toStructure(SchoolEntity schoolEntity);

  FedProvSchoolCodes toStruct(FedProvCode fedProvCode);
}
