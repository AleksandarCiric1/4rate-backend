package com.example.backend4rate.models.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "log")
@Entity
@Data
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "message", nullable = false)
    private String message;
    @Basic
    @Column(name = "level", nullable = false)
    private String level;
    @Basic
    @Column(name = "date", nullable = false)
    private Date date;
    @Basic
    @Column(name = "logger", nullable = false)
    private String logger;
}
