package com.example.backend4rate.models.dto;

import java.util.Date; 

import lombok.Data;

@Data
public class ReservationAvailability {

    private Integer id;
    private Date reservationDate;
    private Integer timeSloth;
    private Integer restaurant_Id;
}
