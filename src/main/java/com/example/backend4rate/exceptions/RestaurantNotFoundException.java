package com.example.backend4rate.exceptions;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException() {
        super("Restaurant not found");
    }
}
