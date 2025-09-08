package com.example.dp.administration.service;

import com.example.dp.administration.mapper.ProfessorMapper;
import com.example.dp.administration.dtos.ProfessorDTO;
import com.example.dp.model.professor.entity.Professor;
import com.example.dp.model.professor.service.ProfessorNotFoundException;
import com.example.dp.model.professor.service.ProfessorService;
import com.example.dp.model.schoolclass.entity.SchoolClass;
import com.example.dp.model.schoolclass.service.SchoolClassNotFoundException;
import com.example.dp.model.schoolclass.service.SchoolClassService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component // sau @Service -application service DDD defineste use cases
public class ManageProfessor {
    // aici se vor folosi serviciile mai mici care fac administrarea entitatilor
    // domain service
    // aduc serviciile din model
    // assign class to professor
    //CRUD operation for professor call ProfessorService
    private static final Logger logger = LogManager.getLogger(ManageProfessor.class);

    private final ProfessorService professorService;
    private final SchoolClassService schoolClassService;
    private final ProfessorMapper professorMapper;

    @Autowired
    public ManageProfessor(ProfessorService professorService, SchoolClassService schoolClassService, ProfessorMapper professorMapper) {
        this.professorService = professorService;
        this.schoolClassService = schoolClassService;
        this.professorMapper = professorMapper;
    }

    @Transactional
    public Professor save(ProfessorDTO professorDto) {
        logger.info("Creating professor with code {}", professorDto.getCode());
        validateProfessorCode(professorDto.getCode());
        return professorService.saveOrUpdate(professorMapper.toCreateEntity(professorDto));
    }

    @Transactional
    public Professor update(Integer professorId, ProfessorDTO professorDto) {
        professorService.findById(professorId.longValue())
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found"));
        validateProfessorCode(professorDto.getCode());
        logger.info("Updating professor with id  {}", professorId);
                professorDto.setId(professorId.longValue());
        return professorService.saveOrUpdate(professorMapper.toUpdateEntity(professorDto));
    }

    public List<Professor> findAll() {
        return professorService.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
//        removeProfessorFromClass(id.longValue());
        professorService.deleteProfessor(id);
    }

    public Set<SchoolClass> getClassesForProfessor(Long professorId) {
        return professorService.getClassesForProfessor(professorId);
    }

    @Transactional
    public void assignProfessorToClasses(Long professorId, Set<Long> classIds) {
        Professor professor = professorService.findById(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found"));

        Set<SchoolClass> classes = new HashSet<>(schoolClassService.findAllById(classIds));
        professor.getClasses().addAll(classes);

        professorService.saveOrUpdate(professor);
        logger.info("Assigned professor to a class {}", professorId);
    }

    public List<ProfessorDTO> getProfessorsByClassAndYear(Integer year, String catalogueCode){
      return professorMapper.toProfessorDTOList(professorService.getProfessorsByCatalogueCodeAndYear(catalogueCode, year));
    }

    private void validateProfessorCode(String code){
        if(professorService.isProfessorByCode(code)) {
            throw new ProfessorAlreadyExistException("Professor with this code already exist: " + code);
        }
    }

    @Transactional
    public void removeProfessorFromClass(Long professorId) {
        Professor professor = professorService.findById(professorId)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor not found"));

        // copy to avoid ConcurrentModificationException
        for (SchoolClass schoolClass : new HashSet<>(professor.getClasses())) {
            schoolClass.removeProfessor(professor);
            if(!schoolClassService.existsById(schoolClass.getId())){
                schoolClassService.saveSchoolClass(schoolClass);
            }

        }
        professorService.delete(professor);
    }
}
