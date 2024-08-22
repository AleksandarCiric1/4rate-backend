package com.example.backend4rate.models.entities;

import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "notification")
@Entity
public class NotificationEntity implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "content", nullable = false)
    private String content;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

    @Basic
    @Column(name = "status", nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "standard_user_id", referencedColumnName = "id", nullable = false)
    private StandardUserEntity standardUser;
}
