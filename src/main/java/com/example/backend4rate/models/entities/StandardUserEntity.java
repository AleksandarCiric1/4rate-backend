package com.example.backend4rate.models.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "standard_user")
@Entity
public class StandardUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Basic
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;

    @OneToOne(mappedBy = "standardUser")
    private GuestEntity guest;

    @OneToOne(mappedBy = "standardUser")
    private ManagerEntity manager;

    @OneToMany(mappedBy = "standardUser")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "standardUser")
    private List<NotificationEntity> notifications;
}
