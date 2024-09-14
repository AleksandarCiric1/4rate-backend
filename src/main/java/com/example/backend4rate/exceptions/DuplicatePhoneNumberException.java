package com.example.backend4rate.exceptions;

public class DuplicatePhoneNumberException extends Exception {
    public DuplicatePhoneNumberException(String message) {
        super(message);
    }

    public DuplicatePhoneNumberException() {
        super("Duplicate phone number");
    }
}
