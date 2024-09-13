package com.example.backend4rate.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {

    private Integer id;
    private Integer value;
    private Integer restaurantId;
    private Integer guestId;
}
