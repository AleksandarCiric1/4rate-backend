package com.example.backend4rate.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("UserAccount is not found");
    }
}
