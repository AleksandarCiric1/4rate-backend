package com.example.backend4rate.models.dto;

import com.example.backend4rate.models.entities.CategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCategory {
    private Integer id;
    private CategoryEntity category;
}
