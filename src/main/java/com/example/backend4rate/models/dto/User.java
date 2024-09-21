package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String role;
    private String status;
    private boolean confirmed;
    private String email;
    private Date createdAt;
    private String avatarUrl;
    private Date dateOfBirth;
    private String firstName;
    private String lastName;
    private ManagerLogin manager;
    private GuestLogin guest;
    private AdminLogin administrator;
}
