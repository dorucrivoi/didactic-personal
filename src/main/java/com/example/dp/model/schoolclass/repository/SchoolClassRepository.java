package com.example.dp.model.schoolclass.repository;

import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    boolean existsByClassCodeAndClassYear(String classCode, Integer classYear);

   @Query(value = """
        SELECT p.* 
        FROM PROFESSOR p 
        JOIN CLASS_PROFESSOR cp 
            ON p.ID = cp.PROFESSOR_ID 
        WHERE cp.CLASS_ID = :classId
        """, nativeQuery = true)
    Set<Professor> findProfessorsByClassId(@Param("classId") Long classId);

}