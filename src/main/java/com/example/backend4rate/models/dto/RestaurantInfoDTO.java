package com.example.backend4rate.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfoDTO {
    private Integer id;
    private String name;
    private String description;
    private String workTime;
    private String status;
    private String address;
    private String city;
    private String country;
}
