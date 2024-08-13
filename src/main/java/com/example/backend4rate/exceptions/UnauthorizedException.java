package com.example.backend4rate.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(){}
    public UnauthorizedException(String message){
        super(message);
    }
}
