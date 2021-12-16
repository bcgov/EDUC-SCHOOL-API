package ca.bc.gov.educ.api.school.mapper.v1;

import ca.bc.gov.educ.api.school.mapper.StringMapper;
import ca.bc.gov.educ.api.school.model.v1.FedProvCodeEntity;
import ca.bc.gov.educ.api.school.struct.v1.FedProvSchoolCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface FedProvCode mapper.
 */
@SuppressWarnings("squid:S1214")
@Mapper(uses = {StringMapper.class})
public interface FedProvCodeMapper {

  /**
   * The constant mapper.
   */
  FedProvCodeMapper mapper = Mappers.getMapper(FedProvCodeMapper.class);

  @Mapping(target = "key", source = "fedProvCodeId.key")
  @Mapping(target = "federalCode", source = "fedProvCodeId.federalCode")
  FedProvSchoolCode toStruct(FedProvCodeEntity fedProvCodeEntity);
}
