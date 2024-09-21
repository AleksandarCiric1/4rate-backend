package com.example.backend4rate.exceptions;

public class EmptyResultDataAccessException extends Exception{
    private String className;

    public EmptyResultDataAccessException(String message, String className){
        super(message);
        this.className = className;
    }

    public EmptyResultDataAccessException(String className){
        super("user account with requested id does not exist!");
        this.className = className;
    }
    public String getClassName(){
        return className;
    }
}
