package com.example.dp.model.professors.repository;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.professor.repository.ProfessorRepository;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProfessorRepositoryTests {

    @Autowired
    private ProfessorRepository professorRepository;

    @Test
    void findClasses_by_professorId() {
        // Professor 1 (John Smith) is assigned to class 1 and 2
        Set<SchoolClass> classes = professorRepository.findClassesByProfessorId(1L);

        assertThat(classes).hasSize(2);
        assertThat(classes)
                .extracting(SchoolClass::getClassCode)
                .containsExactlyInAnyOrder("12C", "11B");
    }

    @Test
    void findAll_by_classCode_and_year() {
        // Class "12C" (2025) has professors 1, 2, 3, 4 assigned
        List<Professor> professors = professorRepository.findAllByClassCodeAndYear("12C", 2025);

        assertThat(professors).hasSize(4);
        assertThat(professors)
                .extracting(Professor::getName)
                .containsExactly("Alice Johnson", "Emily Davis", "John Smith", "Robert Brown"); // sorted by NAME ASC
    }

    @Test
    void verify_if_exists_by_code() {
        assertThat(professorRepository.existsByCode("P001")).isTrue(); // John Smith
        assertThat(professorRepository.existsByCode("P999")).isFalse(); // nonexistent
    }

    @Test
    void findClasses_by_professorId_not_found() {
        Set<SchoolClass> classes = professorRepository.findClassesByProfessorId(999L);

        assertThat(classes).isEmpty();
    }

    @Test
    void findAll_by_classCode_and_year_not_found() {
        // Negative: Class does not exist for that year
        List<Professor> professors = professorRepository.findAllByClassCodeAndYear("99Z", 2030);

        assertThat(professors).isEmpty();
    }
}
