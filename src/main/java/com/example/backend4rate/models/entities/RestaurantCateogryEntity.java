package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "restaurant_category")
@Entity
public class RestaurantCateogryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "resturant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
