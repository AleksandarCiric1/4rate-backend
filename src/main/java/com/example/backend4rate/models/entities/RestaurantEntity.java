package com.example.backend4rate.models.entities;

import java.util.List;
import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "restaurant")
@Entity
public class RestaurantEntity implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "work_time", nullable = false)
    private String workTime;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "city")
    private String city;

    @Basic
    @Column(name = "country")
    private String country;

    @Basic
    @Column(name = "created_at")
    private Date createdAt;

    @Basic
    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne(mappedBy = "restaurant")
    private ManagerEntity manager;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantPhoneEntity> restaurantPhones;

    @OneToMany(mappedBy = "restaurant")
    private List<ImageEntity> images;

    @OneToMany(mappedBy = "restaurant")
    private List<MonthlyReportEntity> monthlyReports;

    @OneToMany(mappedBy = "restaurant")
    private List<ReviewEntity> review;

    @OneToMany(mappedBy = "restaurant")
    private List<ReservationEntity> reservations;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantCategoryEntity> restaurantCategories;

}
