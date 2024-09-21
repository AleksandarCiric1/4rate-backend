package com.example.backend4rate.exceptions;

public class NullPointerException extends Exception{
    private String className;

    public NullPointerException(String className){
        super("FIle does not exist!");
        this.className = className;
    }

    public NullPointerException(String message, String className){
        super(message);
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
