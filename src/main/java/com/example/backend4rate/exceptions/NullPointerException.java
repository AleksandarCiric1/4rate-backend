package com.example.backend4rate.exceptions;

public class NullPointerException extends Exception{
    public NullPointerException(){
        super("FIle does not exist!");
    }

    public NullPointerException(String message){
        super(message);
    }
}
