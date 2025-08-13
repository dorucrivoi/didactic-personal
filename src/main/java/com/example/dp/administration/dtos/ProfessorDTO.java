package com.example.dp.administration.dtos;

public class ProfessorDTO {

    private Long id;
    private String name;
    private String disciplineCode;
    private String professorCode;;

    public ProfessorDTO(Long id, String name, String disciplineCode, String professorCode) {
        this.id = id;
        this.name = name;
        this.disciplineCode = disciplineCode;
        this.professorCode = professorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisciplineCode() {
        return disciplineCode;
    }

    public void setDisciplineCode(String disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessorCode() { return professorCode; }

    public void setProfessorCode(String professorCode) { this.professorCode = professorCode; }
}
