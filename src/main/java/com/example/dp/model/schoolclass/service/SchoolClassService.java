package com.example.dp.model.schoolclass.service;

import com.example.dp.exception.SchoolClassCreatedException;
import com.example.dp.exception.SchoolClassUpdatedException;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.schoolclass.repository.SchoolClassRepository;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;

    @Autowired
    public SchoolClassService(SchoolClassRepository repository) {
        this.schoolClassRepository = repository;
    }

    public SchoolClass createSchoolClass(SchoolClass schoolClass) {
        boolean exists = schoolClassRepository.existsByClassCodeAndClassYear(
                schoolClass.getClassCode(),
                schoolClass.getClassYear()
        );
        if (exists) {
            throw new SchoolClassCreatedException("SchoolClass already exists with code " +
                    schoolClass.getClassCode() + " and year " + schoolClass.getClassYear());
        }
        return schoolClassRepository.save(schoolClass);
    }

    public SchoolClass updateSchoolClass(Long id, SchoolClass updatedData) {
        return schoolClassRepository.findById(id)
                .map(existing -> {
                    existing.setClassCode(updatedData.getClassCode());
                    existing.setClassYear(updatedData.getClassYear());
                    existing.setName(updatedData.getName());
                    return schoolClassRepository.save(existing);
                })
                .orElseThrow(() ->
                        new SchoolClassUpdatedException("SchoolClass not found with id " + id));
    }



//    public SchoolClass saveOrUpdate(SchoolClass schoolClass) {
//        return schoolClassRepository.save(schoolClass);
//    }

    public List<SchoolClass> findAll() {
        return schoolClassRepository.findAll();
    }

    public List<SchoolClass> findAllById(Set<Long> classIds) {
        return schoolClassRepository.findAllById(classIds);
    }

    public Optional<SchoolClass> findById(Long id) {
        return schoolClassRepository.findById(id);
    }

    public void deleteById(Long id) {
        schoolClassRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return schoolClassRepository.existsById(id);
    }

    public Set<Professor> getProfessorsByClassId(Long classId) {
        return schoolClassRepository.findProfessorsByClassId(classId);
    }
}