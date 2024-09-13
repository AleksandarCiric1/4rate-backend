package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private Integer userAccountId;
    private String username;
    private String role;
    private String status;
    private boolean confirmed;
    private String email;
    private Date created_at;
    private String avatar_url;
    private Date date_of_birth;
    private String first_name;
    private String last_name;
}
