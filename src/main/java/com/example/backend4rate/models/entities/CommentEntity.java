package com.example.backend4rate.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "comment")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "standard_user_id", referencedColumnName = "id", nullable = false)
    private StandardUserEntity standardUser;

    
}
