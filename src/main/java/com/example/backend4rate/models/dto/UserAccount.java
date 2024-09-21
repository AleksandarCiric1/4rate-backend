package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class UserAccount {
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
    private String email;
    private String status;
}
