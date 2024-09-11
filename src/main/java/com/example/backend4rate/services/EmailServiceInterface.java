package com.example.backend4rate.services;

public interface EmailServiceInterface {
    void sendEmail(String to, String subject, String body);
}
