package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class ReviewResponse {

    private Integer grade;
    private String comment;
    private String restaurantName;
    private String guestUserAccountUsername;
}
