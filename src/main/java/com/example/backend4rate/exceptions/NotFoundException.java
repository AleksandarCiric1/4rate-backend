package com.example.backend4rate.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("UserAccount is not found");
    }
}
