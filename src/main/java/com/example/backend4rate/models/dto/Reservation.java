package com.example.backend4rate.models.dto;

import lombok.Data;

import java.sql.Time;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class Reservation {

    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date; // front
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private Time time; // front
    private String description; // frot broj gostiju
    private String status;
    // private Integer userAccountId; //userAccountId
    private Guest guest;
    private RestaurantInfoDTO restaurant;
}
