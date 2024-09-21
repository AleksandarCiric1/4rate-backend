package com.example.backend4rate.exceptions;

public class ReservationsFullException extends Exception{
    private String className;

    public ReservationsFullException(String className){
        super("Resource Limit Exceded!");
        this.className = className;
    }
    public ReservationsFullException(String message, String className){
        super(message);
        this.className = className;
    }

    public String getClassName(){
        return className;
    }
}
