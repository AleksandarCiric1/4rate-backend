package com.example.backend4rate.models.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "reservation")
@Entity
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "date", nullable = false)
    private Date date;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id", nullable = false)
    private GuestEntity guest;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
