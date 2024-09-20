package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class RequestForRestaurant{
    private String name;
    private String workTime;
    private String description;
    private Integer capacity;
}
