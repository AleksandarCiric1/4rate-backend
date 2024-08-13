package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "restaurant_phone")
@Entity
public class RestaurantPhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
