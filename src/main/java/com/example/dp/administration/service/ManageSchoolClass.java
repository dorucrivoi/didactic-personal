package com.example.dp.administration.service;

import com.example.dp.administration.mapper.SchoolMapper;
import com.example.dp.messaging.SchoolClassCreatedEvent;
import com.example.dp.messaging.SchoolClassDeletedEvent;
import com.example.dp.messaging.SchoolClassProducer;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.administration.dtos.SchoolClassDTO;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import com.example.dp.model.schoolclass.service.SchoolClassNotFoundException;
import com.example.dp.model.schoolclass.service.SchoolClassService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public class ManageSchoolClass {

    private static final Logger logger = LogManager.getLogger(ManageSchoolClass.class);

    private final SchoolClassService schoolClassService;
    private final SchoolClassProducer schoolClassProducer;
    private final SchoolMapper schoolMapper;

    @Autowired
    public ManageSchoolClass(SchoolClassService schoolClassService, SchoolClassProducer schoolClassProducer,
                             SchoolMapper schoolMapper) {
        this.schoolClassService = schoolClassService;
        this.schoolClassProducer = schoolClassProducer;
        this.schoolMapper = schoolMapper;
    }

    @Transactional
    public SchoolClass save(SchoolClassDTO schoolClassDTO) {
        SchoolClass schoolClass = schoolClassService.saveSchoolClass(schoolMapper.toCreateEntity(schoolClassDTO));
        schoolClassProducer.sendClassCreated(new SchoolClassCreatedEvent(schoolClassDTO.getClassCode(), schoolClassDTO.getClassYear()));
        logger.info("Created school class with code {}", schoolClassDTO.getClassCode());
        return schoolClass;
    }

    @Transactional
    public SchoolClass update(Integer classId, SchoolClassDTO schoolClassDTO) {
        SchoolClass schoolClass = schoolClassService.updateSchoolClass(classId.longValue(), schoolMapper.toUpdateEntity(schoolClassDTO));
        logger.info("Updated school class with code {}", schoolClassDTO.getClassCode());
        return schoolClass;
    }

    public List<SchoolClass> findAll() {
        return schoolClassService.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        SchoolClass schoolClass = schoolClassService.findById(id)
                .orElseThrow(() -> new SchoolClassNotFoundException("School class not found with id: " + id));
        if(!schoolClassService.getProfessorsByClassId(id).isEmpty()){
            throw new ProfessorAssignedToClassException("Class can't be deleted, professors are assigned on it");
        }

        schoolClassService.deleteById(id);
        schoolClassProducer.sendClassDeleted(new SchoolClassDeletedEvent(schoolClass.getClassCode(), schoolClass.getClassYear()));
        logger.info("Deleted school class with id {}", id);
    }

    public Set<Professor> getProfessorsByClassId(Long classId){
        return schoolClassService.getProfessorsByClassId(classId);
    }
}
