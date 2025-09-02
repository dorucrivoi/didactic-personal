package com.example.dp.administration.validator;

import com.example.dp.common.ValidationException;

public class ClassCodeNotValidException extends ValidationException {
    public ClassCodeNotValidException() {
    }

    public ClassCodeNotValidException(String message) {
        super(message);
    }

    public ClassCodeNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
