// package com.example.backend4rate.models.entities;

// import java.util.Date;
// import java.util.List;

// import com.example.backend4rate.base.BaseEntity;

// import jakarta.persistence.*;
// import lombok.Data;

// @Data
// @Table(name = "standard_user")
// @Entity
// public class StandardUserEntity implements BaseEntity<Integer> {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// @Column(name = "id", nullable = false)
// private Integer id;

// @OneToOne
// @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable =
// false)
// private UserAccountEntity userAccount;

// @OneToOne(mappedBy = "standardUser")
// private GuestEntity guest;

// @OneToOne(cascade = CascadeType.ALL, mappedBy = "standardUser")
// private ManagerEntity manager;

// @OneToMany(mappedBy = "standardUser")
// private List<CommentEntity> comments;

// @OneToMany(mappedBy = "standardUser")
// private List<NotificationEntity> notifications;
// }
