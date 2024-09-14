package com.example.backend4rate.exceptions;

public class InvalidPhoneNumberException extends Exception {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException() {
        super("Invalid phone number");
    }
}
