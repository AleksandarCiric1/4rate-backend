package com.example.backend4rate.models.entities;

import java.util.List;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "guest")
@Entity
public class GuestEntity implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "contact")
    private String contact;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;

    @OneToMany(mappedBy = "guest")
    private List<CategorySubscriptionEntity> categorySubscriptions;

    @OneToMany(mappedBy = "guest")
    private List<ReservationEntity> reservations;

    @OneToMany(mappedBy = "guest")
    private List<ReviewEntity> review;
}
