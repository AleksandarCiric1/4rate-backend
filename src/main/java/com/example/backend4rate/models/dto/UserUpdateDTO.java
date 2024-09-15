package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private Integer userAccountId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}
