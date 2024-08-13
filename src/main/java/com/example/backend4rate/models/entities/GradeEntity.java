package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "grade")
@Entity
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id", nullable = false)
    private GuestEntity guest;
}
