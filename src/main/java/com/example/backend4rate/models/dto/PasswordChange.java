package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class PasswordChange {
    private Integer userAccountId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
