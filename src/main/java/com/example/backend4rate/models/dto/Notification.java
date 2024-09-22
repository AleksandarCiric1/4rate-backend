package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer id;
    private String message;
    private Date createdAt;
    private boolean isRead;
    private String title;
    private User userAccount;

    public Notification(Integer userId, String message) {
        this.id = userId;
        this.message = message;
    }
}
