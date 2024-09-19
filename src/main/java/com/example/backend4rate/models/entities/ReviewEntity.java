package com.example.backend4rate.models.entities;

import java.util.Date;

import com.example.backend4rate.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "review")
@Entity
public class ReviewEntity implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Basic
    @Column(name = "comment", nullable = false)
    private String comment;

    @Basic
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id", nullable = false)
    private GuestEntity guest;

}
