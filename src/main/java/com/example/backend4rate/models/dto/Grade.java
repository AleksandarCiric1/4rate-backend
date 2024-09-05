package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class Grade {

    private Integer id;
    private Integer value;
    private Integer restaurantId;
    private Integer guestId;
}
