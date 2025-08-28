package com.example.dp.model.schoolclass.service;

import com.example.dp.common.ValidationException;

public class SchoolClassAlreadyCreatedException extends ValidationException {

    public SchoolClassAlreadyCreatedException() {
    }

    public SchoolClassAlreadyCreatedException(String message) {
        super(message);
    }

    public SchoolClassAlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
