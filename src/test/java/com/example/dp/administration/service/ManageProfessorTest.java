package com.example.dp.administration.service;
import com.example.dp.administration.dtos.ProfessorDTO;
import com.example.dp.administration.mapper.ProfessorMapper;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.professor.service.ProfessorService;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import com.example.dp.model.schoolclass.service.SchoolClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ManageProfessorTest {

    @Mock
    private ProfessorService professorService;

    @Mock
    private SchoolClassService schoolClassService;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private ManageProfessor manageProfessor;

    private ProfessorDTO professorDto;
    private Professor professor;

    @BeforeEach
    void setUp() {
        professorDto = new ProfessorDTO(1L, "John Doe", "MATH", "P123");

        professor = new Professor();
        professor.setId(1L);
        professor.setName("John Doe");
        professor.setDisciplineCode("MATH");
        professor.setCode("P123");
    }

    @Test
    void save_new_professor() {
        when(professorService.isProfessorByCode("P123")).thenReturn(false);
        when(professorMapper.toCreateEntity(professorDto)).thenReturn(professor);
        when(professorService.saveOrUpdate(professor)).thenReturn(professor);

        Professor saved = manageProfessor.save(professorDto);

        assertThat(saved).isEqualTo(professor);
        verify(professorService).saveOrUpdate(professor);
    }

    @Test
    void should_throw_exception_when_save_professor_code_exists() {
        when(professorService.isProfessorByCode("P123")).thenReturn(true);

        assertThatThrownBy(() -> manageProfessor.save(professorDto))
                .isInstanceOf(ProfessorAlreadyExistException.class)
                .hasMessageContaining("Professor with this code already exist");

        verify(professorService, never()).saveOrUpdate(any());
    }

    @Test
    void update_professor() {
        when(professorService.findById(1L)).thenReturn(Optional.of(professor));
        when(professorService.isProfessorByCode("P123")).thenReturn(false);
        when(professorMapper.toUpdateEntity(professorDto)).thenReturn(professor);
        when(professorService.saveOrUpdate(professor)).thenReturn(professor);

        Professor updated = manageProfessor.update(1, professorDto);

        assertThat(updated).isEqualTo(professor);
        verify(professorService).saveOrUpdate(professor);
    }

    @Test
    void should_throw_exception_when_update_and_professor_not_found() {
        when(professorService.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manageProfessor.update(1, professorDto))
                .isInstanceOf(ProfessorNotFoundException.class);

        verify(professorService, never()).saveOrUpdate(any());
    }

    @Test
    void should_throw_exception_when_update_professor_code_exists() {
        when(professorService.findById(1L)).thenReturn(Optional.of(professor));
        when(professorService.isProfessorByCode("P123")).thenReturn(true);

        assertThatThrownBy(() -> manageProfessor.update(1, professorDto))
                .isInstanceOf(ProfessorAlreadyExistException.class);

        verify(professorService, never()).saveOrUpdate(any());
    }

    @Test
    void delete_professor_by_id() {
        manageProfessor.deleteById(1L);
        verify(professorService).deleteProfessor(1L);
    }

    @Test
    void assign_professor_to_classes_professor_id_and_list_of_class_ids() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(100L);

        professor.setClasses(new HashSet<>());
        when(professorService.findById(1L)).thenReturn(Optional.of(professor));
        when(schoolClassService.findAllById(Set.of(100L))).thenReturn(List.of(schoolClass));
        when(professorService.saveOrUpdate(professor)).thenReturn(professor);

        manageProfessor.assignProfessorToClasses(1L, Set.of(100L));

        assertThat(professor.getClasses()).contains(schoolClass);
        verify(professorService).saveOrUpdate(professor);
    }

    @Test
    void should_throw_exception_when_professor_not_exist_to_assign_it_for_classes() {
        when(professorService.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manageProfessor.assignProfessorToClasses(1L, Set.of(100L)))
                .isInstanceOf(ProfessorNotFoundException.class);
    }

    @Test
    void should_assign_nothing_when_classIds_are_empty() {
        when(professorService.findById(1L)).thenReturn(Optional.of(professor));
        professor.setClasses(new HashSet<>());

        manageProfessor.assignProfessorToClasses(1L, Set.of());

        assertThat(professor.getClasses()).isEmpty();
        verify(professorService).saveOrUpdate(professor);
    }

    @Test
    void returns_all_professors_by_classCode_and_year() {
        List<Professor> professors = List.of(professor);
        List<ProfessorDTO> dtos = List.of(professorDto);

        when(professorService.getProfessorsByClassCodeAndYear("11B", 2025)).thenReturn(professors);
        when(professorMapper.toProfessorDTOList(professors)).thenReturn(dtos);

        List<ProfessorDTO> result = manageProfessor.getProfessorsByClassAndYear( "11B", 2025);

        assertThat(result).containsExactly(professorDto);
        verify(professorMapper).toProfessorDTOList(professors);
    }

    @Test
    void should_return_empty_list_when_no_professors_found_by_classCode_and_year() {
        when(professorService.getProfessorsByClassCodeAndYear("XX", 2099)).thenReturn(List.of());
        when(professorMapper.toProfessorDTOList(List.of())).thenReturn(List.of());

        List<ProfessorDTO> result = manageProfessor.getProfessorsByClassAndYear("XX",2099 );

        assertThat(result).isEmpty();
        verify(professorMapper).toProfessorDTOList(List.of());
    }

}
