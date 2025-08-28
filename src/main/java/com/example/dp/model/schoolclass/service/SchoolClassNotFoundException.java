package com.example.dp.model.schoolclass.service;

import com.example.dp.common.ValidationException;

public class SchoolClassNotFoundException extends ValidationException {

    public SchoolClassNotFoundException() {
    }

    public SchoolClassNotFoundException(String message) {
        super(message);
    }

    public SchoolClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
