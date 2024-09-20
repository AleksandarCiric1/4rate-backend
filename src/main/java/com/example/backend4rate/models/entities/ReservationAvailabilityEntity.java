package com.example.backend4rate.models.entities;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Table(name="reservation_availability")
@Entity
public class ReservationAvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    Integer id;

    @Basic
    @Column(name="reservationDate", nullable = false)
    Date reservationDate;

    @Basic
    @Column(name="timeSloth", nullable = false)
    Integer timeSloth;

    @ManyToOne
    @JoinColumn
    (name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;
}
