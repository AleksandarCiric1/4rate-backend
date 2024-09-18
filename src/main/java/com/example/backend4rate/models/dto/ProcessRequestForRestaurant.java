package com.example.backend4rate.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRequestForRestaurant {
    private Integer userAccountId;
    private Integer requestId;
}
