package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateInformation {
    private String username;
    private String avatarUrl;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String contact;
}
    
