package com.example.dp.administration.dtos;

public class ProfessorDTO {

    private Long id;
    private String name;
    private String disciplineCode;
    private String code;;

    public ProfessorDTO(Long id, String name, String disciplineCode, String code) {
        this.id = id;
        this.name = name;
        this.disciplineCode = disciplineCode;
        this.code = code;
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

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}
