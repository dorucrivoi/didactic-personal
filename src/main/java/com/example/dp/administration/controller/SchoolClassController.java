package com.example.dp.administration.controller;


import com.example.api.ClassesApi;
import com.example.dp.administration.validator.Validator;
import com.example.dp.administration.mapper.SchoolMapper;
import com.example.dp.administration.service.ManageSchoolClass;
import com.example.model.CreateSchoolClassRequest;
import com.example.model.ProfessorResponse;
import com.example.model.SchoolClassResponse;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchoolClassController implements ClassesApi {

    private static final Logger logger = LoggerFactory.getLogger(SchoolClassController.class);


    private final ManageSchoolClass manageSchoolClass;
    private final SchoolMapper schoolMapper;
    private final Validator validator;

    private final Counter classCreationCounter;

    @Autowired
    public SchoolClassController(ManageSchoolClass service, SchoolMapper schoolMapper, Validator validator,
            MeterRegistry meterRegistry) {

        this.manageSchoolClass = service;
        this.schoolMapper = schoolMapper;
        this.validator = validator;

        //  global counter
        this.classCreationCounter = Counter.builder("api_admin_classes_creation")
                .description("Total number of created school classes")
                .register(meterRegistry);
    }

    @Override
//    @Timed(value = "didactic.class.create.time", description = "Latency for creating a school class")
//    @Counted(value = "didactic.class.create.count", description = "Number of created school classes")
    public ResponseEntity<Void> createSchoolClass (CreateSchoolClassRequest createSchoolClassRequest) {

        if(!validator.isValid(createSchoolClassRequest.getClassCode())){
            throw new ClassCodeNotValidException("The class code is not valid:" + createSchoolClassRequest.getClassCode());
        }
        manageSchoolClass.save(schoolMapper.toDTO(createSchoolClassRequest));

        // total
        classCreationCounter.increment();

        logger.info("api_admin_classes_creation_total = {}", classCreationCounter.count());
        logger.info("Create school class from controller");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<SchoolClassResponse>> getAllClasses() {
        List<SchoolClassResponse> classes  = schoolMapper.toSchoolClassResponseList(manageSchoolClass.findAll());
        return ResponseEntity.ok(classes);
    }

    @Override
    public ResponseEntity<Void> deleteSchoolClass(Integer id) {
        manageSchoolClass.deleteById(id.longValue());
        logger.info("Delete school class from controller with {}", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ProfessorResponse>> getProfessorsForClassId(Integer id){
        List<ProfessorResponse> professors = schoolMapper.toProfessorResponseList(manageSchoolClass.getProfessorsByClassId(id.longValue()).stream().toList());
        logger.info("Get professors for classId from controller {}", id);
        return ResponseEntity.ok(professors);
    }
}