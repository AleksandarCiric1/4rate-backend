package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.services.impl.ReservationService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    @GetMapping("/getReservation/{reservationId}")
    public Reservation getReservation(@PathVariable Integer reservationId) throws NotFoundException {
        return reservationService.getReservation(reservationId);
    }
    
    @PostMapping("/makeReservation")
    public Reservation makeReservation(@RequestBody Reservation reservation) throws NotFoundException, DuplicateReservationException{
        return reservationService.makeReservation(reservation);
    }
   
    

    @DeleteMapping("delete/{reservationId}")
    public void cancelReservation(@PathVariable Integer reservationId) {
        reservationService.deleteReservation(reservationId);
    }

}
