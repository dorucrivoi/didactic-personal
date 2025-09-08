package com.example.dp.administration.controller;


import com.example.api.ClassesApi;
import com.example.dp.administration.validator.ClassCodeNotValidException;
import com.example.dp.administration.validator.Validator;
import com.example.dp.administration.mapper.SchoolMapper;
import com.example.dp.administration.service.ManageSchoolClass;
import com.example.model.CreateSchoolClassRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/admin")
public class SchoolClassController implements ClassesApi {

    private static final Logger logger = LoggerFactory.getLogger(SchoolClassController.class);


    private final ManageSchoolClass manageSchoolClass;
    private final SchoolMapper schoolMapper;
    private final Validator validator;

    @Autowired
    public SchoolClassController(ManageSchoolClass service, SchoolMapper schoolMapper, Validator validator) {
        this.manageSchoolClass = service;
        this.schoolMapper = schoolMapper;
        this.validator = validator;
    }

    @Override
    public ResponseEntity<Void> createSchoolClass (CreateSchoolClassRequest createSchoolClassRequest) {
        if(!validator.isValid(createSchoolClassRequest.getClassCode())){
            throw new ClassCodeNotValidException("The class code is not valid:" + createSchoolClassRequest.getClassCode());
        }
        manageSchoolClass.save(schoolMapper.toDTO(createSchoolClassRequest));
        logger.info("Create school class from controller");
        return ResponseEntity.ok().build();
    }
//TODO cand vrem sa actualizam clasa cum procedam? mai trebuie procesul ?
    // ce facem cu catalogul?
//TODO te duci la documentatia de referinta -- cand nu gasesc ceva
    // TODO diagrama de secventa doar pentru cazurile de creare clasa si preluare profesorii

//    @Override
//    public ResponseEntity<Void> updateSchoolClass(Integer classId, UpdateSchoolClassRequest createSchoolClassRequest) {
//        manageSchoolClass.update(classId, schoolMapper.toDTO(createSchoolClassRequest));
//        logger.info("Update school class from controller");
//        return ResponseEntity.ok().build();
//    }

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
    public ResponseEntity<List<ProfessorResponse>> getProfessorsForClass(Integer id){
        List<ProfessorResponse> professors = schoolMapper.toProfessorResponseList(manageSchoolClass.getProfessorsByClassId(id.longValue()).stream().toList());
        logger.info("Get professors for classId from controller {}", id);
        return ResponseEntity.ok(professors);
    }
}