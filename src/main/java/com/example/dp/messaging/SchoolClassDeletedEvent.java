package com.example.dp.messaging;

public class SchoolClassDeletedEvent extends SchoolClassEvent{

    public SchoolClassDeletedEvent(String classCode, int year) {
        super(classCode, year);
    }
}
