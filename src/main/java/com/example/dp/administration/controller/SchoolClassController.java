package com.example.dp.administration.controller;


import com.example.api.ClassesApi;
import com.example.dp.administration.mapper.SchoolMapper;
import com.example.dp.administration.service.ManageSchoolClass;
import com.example.model.CreateSchoolClassRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import com.example.model.UpdateSchoolClassRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SchoolClassController implements ClassesApi {

    private static final Logger logger = LoggerFactory.getLogger(SchoolClassController.class);

    private final ManageSchoolClass manageSchoolClass;
    private final SchoolMapper schoolMapper;

    @Autowired
    public SchoolClassController(ManageSchoolClass service, SchoolMapper schoolMapper) {
        this.manageSchoolClass = service;
        this.schoolMapper =schoolMapper;
    }

    @Override
    public ResponseEntity<Void> createSchoolClass (CreateSchoolClassRequest createSchoolClassRequest) {
        manageSchoolClass.save(schoolMapper.toDTO(createSchoolClassRequest));
        logger.info("Create school class from controller");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateSchoolClass(Integer classId, UpdateSchoolClassRequest createSchoolClassRequest) {
        manageSchoolClass.update(classId, schoolMapper.toDTO(createSchoolClassRequest));
        logger.info("Update school class from controller");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<SchoolClassResponse>> getAllClasses() {
        List<SchoolClassResponse> classes  = schoolMapper.toSchoolClassResponseList(manageSchoolClass.findAll());
        return ResponseEntity.ok(classes);
    }

    @Override
    public ResponseEntity<Void> deleteClass(Integer id) {
        manageSchoolClass.deleteById(id.longValue());
        logger.info("Delete school class from controller with {}", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ProfessorResponse>> getProfessorsForClass(Integer classId){
        List<ProfessorResponse> professors = schoolMapper.toProfessorResponseList(manageSchoolClass.getProfessorsByClassId(classId.longValue()).stream().toList());
        logger.info("Get professors for classId from controller {}", classId);
        return ResponseEntity.ok(professors);
    }
}