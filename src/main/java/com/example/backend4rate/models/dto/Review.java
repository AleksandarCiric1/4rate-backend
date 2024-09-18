package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class Review {

    private Integer id;
    private Integer grade;
    private String comment;
    private Integer restaurantId;
    private Integer guestId;
}
