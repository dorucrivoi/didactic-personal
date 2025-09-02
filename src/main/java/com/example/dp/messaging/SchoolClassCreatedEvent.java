package com.example.dp.messaging;

public class SchoolClassCreatedEvent extends SchoolClassEvent{

    public SchoolClassCreatedEvent(String classCode, int year) {
        super(classCode, year);
    }
}
