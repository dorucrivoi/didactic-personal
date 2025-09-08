package com.example.dp.administration.service;

import com.example.dp.common.ValidationException;

public class ProfessorAlreadyExistException extends ValidationException {

    public ProfessorAlreadyExistException() {
    }

    public ProfessorAlreadyExistException(String message) {
        super(message);
    }

    public ProfessorAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
