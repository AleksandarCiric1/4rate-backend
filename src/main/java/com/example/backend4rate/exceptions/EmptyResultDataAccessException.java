package com.example.backend4rate.exceptions;

public class EmptyResultDataAccessException extends Exception{
    public EmptyResultDataAccessException(String message){
        super(message);
    }

    public EmptyResultDataAccessException(){
        super("user account with requested id does not exist!");
    }
}
