package com.example.backend4rate.models.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "monthly_report")
@Entity
public class MonthlyReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "number_of_resrvations", nullable = false)
    private Integer numberOfReservations;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
