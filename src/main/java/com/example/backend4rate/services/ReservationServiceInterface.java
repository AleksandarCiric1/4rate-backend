package com.example.backend4rate.services;
import java.util.List;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;

public interface ReservationServiceInterface {

    Reservation getReservation(Integer reservationId) throws NotFoundException;
   // List<Reservation> getAllGuestReservation(Integer guestId);
   // List<Reservation> getAllRestaurantReservation(Integer restaurant);
    void deleteReservation(Integer reservationId);
    Reservation makeReservation(Reservation reservation) throws NotFoundException, DuplicateReservationException;    

}
