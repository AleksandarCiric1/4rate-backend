package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name = "location")
@Entity
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "city", nullable = false)
    private String city;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;
}
