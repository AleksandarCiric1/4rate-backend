package com.example.backend4rate.models.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Integer id;
    private String comment;
    private Integer grade;
    private Guest guest;
    private Integer restaurantId;
    private Date createdAt;
}
