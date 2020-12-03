package ca.bc.gov.educ.api.school.mapper;

import ca.bc.gov.educ.api.school.model.SchoolEntity;
import ca.bc.gov.educ.api.school.struct.v1.School;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LocalDateTimeMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMapper {

    SchoolMapper mapper = Mappers.getMapper(SchoolMapper.class);

    SchoolEntity toModel(School school);

    @Mapping(target = "distNo", source="schoolEntity.mincode.distNo")
    @Mapping(target = "schlNo", source="schoolEntity.mincode.schlNo")
    School toStructure(SchoolEntity schoolEntity);
}
