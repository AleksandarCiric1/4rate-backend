package com.example.backend4rate.exceptions;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException(String message) {
        super(message);
    }
    public AlreadySubscribedException() {
        super("Already subscribed to category");
    }
}
