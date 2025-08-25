package com.example.dp.exception;

public class SchoolClassCreatedException extends RuntimeException{

    public SchoolClassCreatedException() {
    }

    public SchoolClassCreatedException(String message) {
        super(message);
    }

    public SchoolClassCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
