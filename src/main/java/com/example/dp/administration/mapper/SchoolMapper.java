package com.example.dp.administration.mapper;

import com.example.dp.model.professor.entity.Professor;
import com.example.dp.administration.dtos.SchoolClassDTO;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import com.example.model.CreateSchoolClassRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import com.example.model.UpdateSchoolClassRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    @Mapping(target = "id", ignore = true)
    SchoolClassDTO toDTO(CreateSchoolClassRequest request);

    SchoolClassDTO toDTO(UpdateSchoolClassRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professors", ignore = true)
    com.example.dp.model.schoolclass.entity.SchoolClass toCreateEntity(SchoolClassDTO dto);

    @Mapping(target = "professors", ignore = true)
    com.example.dp.model.schoolclass.entity.SchoolClass toUpdateEntity(SchoolClassDTO dto);

    List<ProfessorResponse> toProfessorResponseList(List<Professor> entities);

    List<SchoolClassResponse> toSchoolClassResponseList(List<SchoolClass> schoolClasses);
}
