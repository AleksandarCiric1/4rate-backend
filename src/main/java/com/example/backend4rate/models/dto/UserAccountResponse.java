package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class UserAccountResponse {
    private Integer id;
    private String username;
    private String role;
    private String email;
}
