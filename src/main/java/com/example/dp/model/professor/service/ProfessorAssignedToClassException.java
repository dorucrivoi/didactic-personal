package com.example.dp.model.professor.service;

import com.example.dp.common.ValidationException;

public class ProfessorAssignedToClassException extends ValidationException {

    public ProfessorAssignedToClassException() {
    }

    public ProfessorAssignedToClassException(String message) {
        super(message);
    }

    public ProfessorAssignedToClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
