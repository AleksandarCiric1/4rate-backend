package com.example.backend4rate.models.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "category")
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<CategorySubscriptionEntity> categorySubscriptions;

    @OneToMany(mappedBy = "category")
    private List<RestaurantCateogryEntity> restaurantCategories;
}
