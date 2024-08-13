package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "image")
@Entity
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable =false)
    private RestaurantEntity restaurant;
}
