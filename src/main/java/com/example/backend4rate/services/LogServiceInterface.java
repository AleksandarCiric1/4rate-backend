package com.example.backend4rate.services;

public interface LogServiceInterface {

    void error(String message, String logger);
    void info(String message, String logger);
    void warning(String message, String logger);
}
