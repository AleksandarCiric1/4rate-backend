package com.example.backend4rate.models.entities;

import java.util.List;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "request_for_restaurant")
@Entity
public class RequestForRestaurantEntity implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name_of_restaurant", nullable = false)
    private String name;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "work_time", nullable = false)
    private String workTime;
    
    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName =  "id")
    private ManagerEntity manager;

    @OneToMany(mappedBy = "requestForRestaurant")
    private List<RestaurantPhoneEntity> restaurantPhones;

    @OneToMany(mappedBy = "requestForRestaurant")
    private List<ImageEntity> images;

    @OneToMany(mappedBy = "requestForRestaurant")
    private List<RestaurantCategoryEntity> restaurantCategories;
}
