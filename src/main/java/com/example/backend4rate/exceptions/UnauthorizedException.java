package com.example.backend4rate.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message){
        super(message);
    }

    public UnauthorizedException(){
        super("Password is not valid!");
    }
}
