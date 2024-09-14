package com.example.backend4rate.exceptions;
public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
    
public SubscriptionNotFoundException() {
        super("Subscription not found");
    }
}
