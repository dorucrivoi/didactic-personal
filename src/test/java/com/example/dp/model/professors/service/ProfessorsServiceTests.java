package com.example.dp.model.professors.service;

import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.professor.repository.ProfessorRepository;
import com.example.dp.model.professor.service.ProfessorService;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorsServiceTests {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    void should_save_professor_when_saveOrUpdate_is_executed() {
        Professor professor = new Professor();
        when(professorRepository.save(professor)).thenReturn(professor);

        Professor result = professorService.saveOrUpdate(professor);

        assertThat(result).isEqualTo(professor);
        verify(professorRepository).save(professor);
    }

    @Test
    void should_return_null_when_saveOrUpdate_returns_null() {
        Professor professor = new Professor();
        when(professorRepository.save(professor)).thenReturn(null);

        Professor result = professorService.saveOrUpdate(professor);

        assertThat(result).isNull();
        verify(professorRepository).save(professor);
    }

    @Test
    void should_return_all_professors_when_executing_findAll() {
        List<Professor> professors = List.of(new Professor());
        when(professorRepository.findAll()).thenReturn(professors);

        List<Professor> result = professorService.findAll();

        assertThat(result).hasSize(1);
        verify(professorRepository).findAll();
    }

    @Test
    void should_return_empty_list_when_no_professors_found() {
        when(professorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Professor> result = professorService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void should_remove_professor_from_classes_and_then_delete_Professor() {
        Professor professor = new Professor();
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setProfessors(new HashSet<>(Set.of(professor)));
        professor.setClasses(new HashSet<>(Set.of(schoolClass)));

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        professorService.deleteProfessor(1L);

        assertThat(professor.getClasses()).isEmpty();
        verify(professorRepository).delete(professor);
    }

    @Test
    void should_do_nothing_when_professor_not_found_on_delete() {
        when(professorRepository.findById(999L)).thenReturn(Optional.empty());

        professorService.deleteProfessor(999L);

        verify(professorRepository, never()).delete(any());
    }

    @Test
    void should_return_true_if_exists() {
        when(professorRepository.existsById(1L)).thenReturn(true);

        boolean result = professorService.existsById(1L);

        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_if_professor_does_not_exist() {
        when(professorRepository.existsById(2L)).thenReturn(false);

        boolean result = professorService.existsById(2L);

        assertThat(result).isFalse();
    }

    @Test
    void should_return_true_if_professor_exists_by_code() {
        when(professorRepository.existsByCode("ABC")).thenReturn(true);

        boolean result = professorService.isProfessorByCode("ABC");

        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_if_professor_does_not_exist_by_code() {
        when(professorRepository.existsByCode("XYZ")).thenReturn(false);

        boolean result = professorService.isProfessorByCode("XYZ");

        assertThat(result).isFalse();
    }

    @Test
    void returns_all_classes_for_professor_by_id() {
        Set<SchoolClass> classes = Set.of(new SchoolClass());
        when(professorRepository.findClassesByProfessorId(1L)).thenReturn(classes);

        Set<SchoolClass> result = professorService.getClassesForProfessor(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void returns_empty_classes_when_professor_not_assigned_to_any() {
        when(professorRepository.findClassesByProfessorId(2L)).thenReturn(Collections.emptySet());

        Set<SchoolClass> result = professorService.getClassesForProfessor(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void returns_professors_for_catalogueCode_and_year_() {
        List<Professor> professors = List.of(new Professor());
        when(professorRepository.findAllByClassCodeAndYear("CAT", 2024)).thenReturn(professors);

        List<Professor> result = professorService.getProfessorsByClassCodeAndYear("CAT", 2024);

        assertThat(result).hasSize(1);
    }

    @Test
    void returns_empty_list_when_no_professors_found_for_classCode_and_year() {
        when(professorRepository.findAllByClassCodeAndYear("NONE", 2030)).thenReturn(Collections.emptyList());

        List<Professor> result = professorService.getProfessorsByClassCodeAndYear("NONE", 2030);

        assertThat(result).isEmpty();
    }
}
