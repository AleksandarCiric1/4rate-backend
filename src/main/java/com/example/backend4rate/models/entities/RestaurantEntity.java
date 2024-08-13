package com.example.backend4rate.models.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "restaurant")
@Entity
public class RestaurantEntity {

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

    @OneToOne(mappedBy = "restaurant")
    private ManagerEntity manager;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantPhoneEntity> restaurantPhones;

    @OneToMany(mappedBy = "restaurant")
    private List<ImageEntity> images;

    @OneToMany(mappedBy = "restaurant")
    private List<MonthlyReportEntity> monthlyReports;

    @OneToMany(mappedBy = "restaurant")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "restaurant")
    private List<ReservationEntity> reservations;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantCateogryEntity> restaurantCategories;

    @OneToMany(mappedBy = "restaurant")
    private List<GradeEntity> grades;
}
