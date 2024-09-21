package com.example.backend4rate.exceptions;

public class BadRequestException  extends Exception{
    private String className;

    public BadRequestException(String className){
        super("Request is not valid!");
        this.className = className;
    }

    public BadRequestException(String message, String className){
        super(message);
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
