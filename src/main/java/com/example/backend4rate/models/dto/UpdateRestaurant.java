package com.example.backend4rate.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestaurant {
    private Integer id;
    private String description;
    private String workTime;
    private String address;
    private String city;
    private String country;
    private List<String> phones;
    private List<String> categoryIds;
}
