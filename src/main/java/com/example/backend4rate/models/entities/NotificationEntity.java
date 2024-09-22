package com.example.backend4rate.models.entities;

import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "notification")
@Entity
public class NotificationEntity implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "message", nullable = false)
    private String message;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Basic
    @Column(name = "notification_type", nullable = false)
    private String notificationType;

    @ManyToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;
}
