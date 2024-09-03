package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class RequestForRestaurantResponse {
    private Integer id;
    private String name;
    private String workTime;
    private String description;
}
