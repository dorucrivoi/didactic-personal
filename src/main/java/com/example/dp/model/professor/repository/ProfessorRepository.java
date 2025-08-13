package com.example.dp.model.professor.repository;


import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.dp.model.professor.entity.Professor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

//  @Query("SELECT p.classes FROM Professor p WHERE p.id = :professorId")
@Query(value = """
        SELECT sc.* 
        FROM SCHOOL_CLASS sc 
        JOIN CLASS_PROFESSOR cp 
          ON sc.ID = cp.CLASS_ID 
        WHERE cp.PROFESSOR_ID = :professorId
    """, nativeQuery = true)
  Set<SchoolClass> findClassesByProfessorId(@Param("professorId") Long professorId);

  @Query(
          value = "SELECT p.* " +
                  "FROM PROFESSOR p " +
                  "JOIN CLASS_PROFESSOR cp " +
                  "   ON p.ID = cp.PROFESSOR_ID " +
                  "JOIN SCHOOL_CLASS sc " +
                  "   ON cp.CLASS_ID = sc.ID " +
                  "WHERE sc.CLASS_CODE = :classCode " +
                  "  AND sc.CLASS_YEAR = :classYear " +
                  "ORDER BY p.NAME ASC",
          nativeQuery = true
  )
  List<Professor> findAllByClassCodeAndYear(@Param("classCode") String classCode,
                                            @Param("classYear") Integer classYear);


  //  @Query("SELECT c.professors FROM SchoolClass c WHERE c.id = :classId")
//    @Query("SELECT p " +
//            "FROM Professor p " +
//             "JOIN p.classes c " +
//             "WHERE c.id = :classId")
//    Set<Professor> findProfessorsByClassId(@Param("classId") Long classId);

//    @Query("SELECT p.classes FROM Professor p WHERE p.id = :professorId")
//    Set<SchoolClass> findClassesByProfessorId(@Param("professorId") Long professorId);
}