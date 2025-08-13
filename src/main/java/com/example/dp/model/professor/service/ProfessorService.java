package com.example.dp.model.professor.service;


import com.example.dp.model.professor.repository.ProfessorRepository;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dp.model.professor.entity.Professor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository repository) {
        this.professorRepository = repository;
    }

    public Professor saveOrUpdate(Professor professor) {
        return professorRepository.save(professor);
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Optional<Professor> findById(Long id) {
        return professorRepository.findById(id);
    }

    public void deleteById(Long id) {
        professorRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return professorRepository.existsById(id);
    }

    public Set<SchoolClass> getClassesForProfessor(Long professorId) {
        return professorRepository.findClassesByProfessorId(professorId);
    }

    public List<Professor> getProfessorsByCatalogueCodeAndYear(String catalogueCode, Integer year){
        return professorRepository.findAllByClassCodeAndYear(catalogueCode, year);
    }
}