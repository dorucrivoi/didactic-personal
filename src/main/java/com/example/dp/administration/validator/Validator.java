package com.example.dp.administration.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validator {

    private static final Pattern PATTERN = Pattern.compile("^(?:[1-9]|1[0-2])[A-H]$");

    public boolean isValid(String input) {
        return PATTERN.matcher(input).matches();
    }
}
