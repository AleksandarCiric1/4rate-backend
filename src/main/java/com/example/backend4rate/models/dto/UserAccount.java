package com.example.backend4rate.models.dto;

import java.util.Date;
import lombok.Data;

@Data
public class UserAccount {
    private Integer id;
    private String username;
    private String password;
    private boolean status;
    private String role;
    private String email;
    private Date createdAt;
    private String avatarUrl;
}
