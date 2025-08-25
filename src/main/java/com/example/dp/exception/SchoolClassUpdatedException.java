package com.example.dp.exception;

public class SchoolClassUpdatedException extends  RuntimeException {

    public SchoolClassUpdatedException() {
    }

    public SchoolClassUpdatedException(String message) {
        super(message);
    }

    public SchoolClassUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
