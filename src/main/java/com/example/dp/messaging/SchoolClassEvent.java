package com.example.dp.messaging;

public  abstract class SchoolClassEvent {

    private String classCode;
    private int year;

    public SchoolClassEvent(String classCode, int year) {
        this.classCode = classCode;
        this.year = year;
    }

    // getters and setters
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
