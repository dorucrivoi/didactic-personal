package com.example.dp.administration.dtos;

public class SchoolClassDTO {

    private Long id;
    private String name;
    private String classCode;
    private int classYear;

    public SchoolClassDTO(Long id, String name, String classCode, int classYear) {
        this.id = id;
        this.name = name;
        this.classCode = classCode;
        this.classYear = classYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getClassYear() {
        return classYear;
    }

    public void setClassYear(int classYear) {
        this.classYear = classYear;
    }
}
