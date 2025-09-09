package com.example.dp.administration.service;

import com.example.dp.common.ValidationException;

public class ProfessorNotFoundException extends ValidationException {

    public ProfessorNotFoundException() {
    }

    public ProfessorNotFoundException(String message) {
        super(message);
    }

    public ProfessorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
