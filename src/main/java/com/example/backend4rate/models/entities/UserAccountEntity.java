package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

@Data
@Table(name = "user_account")
@Entity
public class UserAccountEntity implements BaseEntity<Integer>{
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
    @Column(name = "email", nullable = false)
    private String email;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Basic
    @Column(name = "avatar_url",  nullable = true )
    private String avatarUrl;

    @OneToOne(mappedBy = "userAccount")
    private AdministratorEntity administrator;
    
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private StandardUserEntity standardUser;
}
