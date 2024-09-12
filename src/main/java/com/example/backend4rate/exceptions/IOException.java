package com.example.backend4rate.exceptions;

public class IOException extends Exception {
    public IOException(){
        super("Resource is not available!");
    }

    public IOException(String message){
        super(message);
    }
}
