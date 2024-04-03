package ca.bc.gov.educ.api.school.mapper.v1;

import ca.bc.gov.educ.api.school.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.school.mapper.StringMapper;
import ca.bc.gov.educ.api.school.model.v1.SchoolFundingGroupEntity;
import ca.bc.gov.educ.api.school.struct.v1.SchoolFundingGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolFundingGroupMapper {

  SchoolFundingGroupMapper mapper = Mappers.getMapper(SchoolFundingGroupMapper.class);

  @Mapping(target = "distNo", source = "schoolFundingGroupEntity.schoolFundingGroupID.distNo")
  @Mapping(target = "schlNo", source = "schoolFundingGroupEntity.schoolFundingGroupID.schlNo")
  @Mapping(target = "fundingGroupCode", source = "schoolFundingGroupEntity.schoolFundingGroupID.fundingGroupCode")
  @Mapping(target = "fundingGroupSubCode", source = "schoolFundingGroupEntity.schoolFundingGroupID.fundingGroupSubCode")
  @Mapping(target = "mincode", expression = "java(org.apache.commons.lang3.StringUtils.trimToEmpty(schoolFundingGroupEntity.getSchoolFundingGroupID().getDistNo()).concat(org.apache.commons.lang3.StringUtils.trimToEmpty(schoolFundingGroupEntity.getSchoolFundingGroupID().getSchlNo())))")
  SchoolFundingGroup toStructure(SchoolFundingGroupEntity schoolFundingGroupEntity);

}
