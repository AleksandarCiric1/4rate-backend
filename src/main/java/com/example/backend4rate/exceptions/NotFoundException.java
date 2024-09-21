package com.example.backend4rate.exceptions;

public class NotFoundException extends Exception {
    private String className;

    public NotFoundException(String message, String className) {
        super(message);
        this.className = className;
    }

    public NotFoundException(String className) {
        super("UserAccount is not found");
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
