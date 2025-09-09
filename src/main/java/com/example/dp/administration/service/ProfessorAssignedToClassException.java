package com.example.dp.administration.service;

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
