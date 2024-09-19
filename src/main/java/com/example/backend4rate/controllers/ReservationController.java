package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.services.impl.ReservationService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }

    @GetMapping("/getReservation/{reservationId}")
    public Reservation getReservation(@PathVariable Integer reservationId) throws NotFoundException {
        return reservationService.getReservation(reservationId);
    }
    
    @GetMapping("/getAllGuestReservations/{guestId}")
    public List<Reservation> getAllGuestReservations(@PathVariable Integer guestId) throws NotFoundException{
        return reservationService.getAllGuestReservations(guestId);
    }

    @GetMapping("/getAllRestaurantReservations/{restaurantId}")
    public List<Reservation> getAllRestaurantReservations(@PathVariable Integer restaurantId) throws NotFoundException{
        return reservationService.getAllRestaurantReservations(restaurantId);
    }

    @PostMapping("/makeReservation")
    public Reservation makeReservation(@RequestBody Reservation reservation) throws NotFoundException, DuplicateReservationException{
        return reservationService.makeReservation(reservation);
    }

    @PutMapping("/approveReservation/{reservationId}")
    public Reservation apporoveReservation(@PathVariable Integer reservationId) throws NotFoundException{
        return reservationService.approveReservation(reservationId);
    }

    @PutMapping("/denyReservation/{reservationId}")
    public Reservation denyReservation(@PathVariable Integer reservationId) throws NotFoundException{
        return reservationService.denyReservation(reservationId);
    }

    @PutMapping("/cancelReservation/{reservationId}")
    public Reservation cancelReservation(@PathVariable Integer reservationId) throws NotFoundException{
        return reservationService.cancelReservation(reservationId);
    } 
    
    @GetMapping("/getNumberOfReservationsByMonth/{restaurantId}/{month}/{year}")
    public Long numberOfReservationByMonth(@PathVariable Integer restaurantId, @PathVariable Integer month, @PathVariable Integer year){
        return reservationService.numberOfReservationsByMonth(restaurantId, month, year);
    }


}
