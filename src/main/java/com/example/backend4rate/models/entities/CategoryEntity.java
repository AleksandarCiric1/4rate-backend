package com.example.backend4rate.models.entities;

import java.util.List;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "category")
@Entity
public class CategoryEntity implements BaseEntity<Integer>{
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
    private List<RestaurantCategoryEntity> restaurantCategories;
}
