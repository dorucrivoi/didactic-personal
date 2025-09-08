package com.example.dp.administration.controller;


import com.example.api.ProfessorsApi;
import com.example.dp.administration.mapper.ProfessorMapper;
import com.example.dp.administration.service.ManageProfessor;
import com.example.model.CreateProfessorRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import com.example.model.UpdateProfessorRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/api")
public class ProfessorController implements ProfessorsApi {
    private static final Logger logger = LoggerFactory.getLogger(ProfessorController.class);

    private final ManageProfessor manageProfessor;
    private final ProfessorMapper professorMapper;

    @Autowired
    public ProfessorController(ManageProfessor manageProfessor, ProfessorMapper professorMapper) {
        this.manageProfessor = manageProfessor;
        this.professorMapper = professorMapper;
    }

    @Override
    public ResponseEntity<Void> createProfessor(CreateProfessorRequest professor) {
        manageProfessor.save(professorMapper.toDTO(professor));
        logger.info("Create professor from controller");
        return ResponseEntity.ok().build();
    }
    //DTO-urile se pot exclude

    @Override
    public ResponseEntity<Void> updateProfessor(Integer id, UpdateProfessorRequest professor) {
        manageProfessor.update(id, professorMapper.toDTO(professor));
        logger.info("Update professor from controller");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteProfessor(Integer id) {
        manageProfessor.deleteById(id.longValue());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<SchoolClassResponse>> getClassesForProfessor(Integer id) {
        List<SchoolClassResponse> classes = professorMapper.toSchoolClassResponseList(manageProfessor.getClassesForProfessor(id.longValue()));
        return ResponseEntity.ok(classes);
    }

    @Override
    public ResponseEntity<List<ProfessorResponse>> getAllProfessors() {
        List<ProfessorResponse> professors = professorMapper.toProfessorResponseList(manageProfessor.findAll());
        return ResponseEntity.ok(professors);
    }

    @Override
    public ResponseEntity<Void> assignProfessorToClasses(Integer professorId, List<Integer> classIds) {
        Set<Long> classIdsLong = classIds.stream()
                .filter(Objects::nonNull)
                .map(Integer::longValue)
                .collect(Collectors.toSet());
        manageProfessor.assignProfessorToClasses(professorId.longValue(), classIdsLong);
        logger.info("Assign professor to classes from controller");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ProfessorResponse>> getAllProfessorsForStudent(String classCode, Integer year){
        List<ProfessorResponse> professors = professorMapper.toProfessorResponseListFromDTOs(
                manageProfessor.getProfessorsByClassAndYear(year, classCode));
        logger.info("Get all professors from a class");
        return ResponseEntity.ok(professors);
    }
}