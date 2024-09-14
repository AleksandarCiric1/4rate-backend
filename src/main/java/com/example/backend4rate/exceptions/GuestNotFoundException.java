package com.example.backend4rate.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String message) {
        super(message);
    }

public GuestNotFoundException() {
        super("Guest not found");
    }
}
