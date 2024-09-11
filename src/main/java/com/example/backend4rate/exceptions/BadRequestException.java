package com.example.backend4rate.exceptions;

public class BadRequestException  extends Exception{
    public BadRequestException(){
        super("Request is not valid!");
    }

    public BadRequestException(String message){
        super(message);
    }
}
