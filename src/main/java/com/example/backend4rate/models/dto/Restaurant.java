package com.example.backend4rate.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private Integer id;
    private String name;
    private String description;
    private String workTime;
    private String status;
    private String address;
    private String city;
    private String country;
    private ManagerDTO manager;
    private List<RestaurantPhone> restaurantPhones;
    private List<Image> images;
    private List<Comment> comments;
    private List<ReviewDTO> reviews;
    private List<ReservationDTO> reservations;
    private List<RestaurantCategory> restaurantCategories;
    private List<Grade> grades;
}
