package com.example.backend4rate.exceptions;

public class InvalidGradeException extends Exception {
    private String className;

    public InvalidGradeException(String message, String className) {
        super(message);
        this.className = className;
    }

    public InvalidGradeException(String className) {
        super("Grade should be in bounds 1-5");
        this.className = className;
    }
    
    public String getClassName(){
        return className;
    }
}

