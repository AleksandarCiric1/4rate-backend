package com.example.backend4rate.exceptions;

public class UnauthorizedException extends Exception{
    private String className;

    public UnauthorizedException(String message, String className){
        super(message);
        this.className = className;
    }

    public UnauthorizedException(String className){
        super("Password is not valid!");
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
