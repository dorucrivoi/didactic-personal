package com.example.dp.administration.mapper;

import com.example.dp.administration.dtos.ProfessorDTO;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import com.example.model.CreateProfessorRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import com.example.model.UpdateProfessorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classes", ignore = true)
    com.example.dp.model.professor.entity.Professor toCreateEntity(ProfessorDTO dto);

    @Mapping(target = "classes", ignore = true)
    com.example.dp.model.professor.entity.Professor toUpdateEntity(ProfessorDTO dto);

    @Mapping(target = "id", ignore = true)
    ProfessorDTO toDTO(CreateProfessorRequest request);

    ProfessorDTO toDTO(UpdateProfessorRequest request);

    List<ProfessorResponse> toProfessorResponseList(List<com.example.dp.model.professor.entity.Professor> entities);

    List<ProfessorResponse> toProfessorResponseListFromDTOs(List<ProfessorDTO> dtos);

    List<ProfessorDTO> toProfessorDTOList(List<com.example.dp.model.professor.entity.Professor> entities);

    ProfessorResponse toProfessorResponse(Professor professor);

    List<SchoolClassResponse> toSchoolClassResponseList(Set<SchoolClass> schoolClasses);

    ProfessorResponse toProfessorResponse(ProfessorDTO dto);

}
