package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "manager")
@Entity
public class ManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "contact", nullable =  false)
    private String contact;

    @OneToOne
    @JoinColumn(name = "standard_user_id", referencedColumnName = "id", nullable = false)
    private StandardUserEntity standardUser;

    @OneToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
