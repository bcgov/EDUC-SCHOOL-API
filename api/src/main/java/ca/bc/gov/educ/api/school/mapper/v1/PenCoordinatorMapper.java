package ca.bc.gov.educ.api.school.mapper.v1;

import ca.bc.gov.educ.api.school.mapper.StringMapper;
import ca.bc.gov.educ.api.school.model.v1.PenCoordinatorEntity;
import ca.bc.gov.educ.api.school.struct.v1.PenCoordinator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StringMapper.class})
public interface PenCoordinatorMapper {
  PenCoordinatorMapper mapper = Mappers.getMapper(PenCoordinatorMapper.class);

  @Mapping(target = "schoolNumber", source = "mincode.schlNo")
  @Mapping(target = "districtNumber", source = "mincode.distNo")
  @Mapping(target = "mincode", expression = "java(org.apache.commons.lang3.StringUtils.trimToEmpty(entity.getMincode().getDistNo()).concat(org.apache.commons.lang3.StringUtils.trimToEmpty(entity.getMincode().getSchlNo())))")
  PenCoordinator toStruct(PenCoordinatorEntity entity);

  @Mapping(target = "mincode", expression = "java(ca.bc.gov.educ.api.school.model.v1.Mincode.builder().distNo(penCoordinator.getDistrictNumber()).schlNo(penCoordinator.getSchoolNumber()).build())")
  PenCoordinatorEntity toModel(PenCoordinator penCoordinator);
}
