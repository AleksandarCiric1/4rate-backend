package com.example.backend4rate.exceptions;

public class InvalidGradeException extends Exception {
    public InvalidGradeException(String message) {
        super(message);
    }

    public InvalidGradeException() {
        super("Grade should be in bounds 1-5");
    }
}

