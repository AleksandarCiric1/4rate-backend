package com.example.backend4rate.models.entities;

import java.util.Date;
import java.util.List;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "user_account")
@Entity
public class UserAccountEntity implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "status", nullable = false)
    private String status;

    @Basic
    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Basic
    @Column(name = "role", nullable = false)
    private String role;

    @Basic
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Basic
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Basic
    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;

    @OneToOne(mappedBy = "userAccount")
    private AdministratorEntity administrator;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private GuestEntity guest;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private ManagerEntity manager;

    @OneToMany(mappedBy = "userAccount")
    private List<NotificationEntity> notifications;
}
