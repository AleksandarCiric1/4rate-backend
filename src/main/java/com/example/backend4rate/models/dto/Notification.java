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
    private String notificationType;
    private Integer userAccountId;
    private User userAccount;

    public Notification(Integer userId, String message) {
        this.id = userId;
        this.message = message;
    }

    public Notification(Integer userId, String message, String title) {
        this.id = userId;
        this.message = message;
        this.title = title;
    }

    public Notification(Integer userAccountId, String message, String title, String notificationType) {
        this.userAccountId = userAccountId;
        this.message = message;
        this.title = title;
        this.notificationType = notificationType;
    }
}
