package com.example.backend4rate.models.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "guest")
@Entity
public class GuestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "contact", nullable = false)
    private String contact;

    @OneToOne
    @JoinColumn(name = "standard_user_id", referencedColumnName = "id", nullable = false)
    private StandardUserEntity standardUser;

    @OneToMany(mappedBy = "guest")
    private List<CategorySubscriptionEntity> categorySubscriptions;

    @OneToMany(mappedBy = "guest")
    private List<ReservationEntity> reservations;

    @OneToMany(mappedBy = "guest")
    private List<GradeEntity> grades;
}
