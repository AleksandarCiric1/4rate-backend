package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.UpdateRestaurant;

public interface ReservationServiceInterface {

    Reservation getReservation(Integer reservationId) throws NotFoundException;

    List<Reservation> getAllGuestReservations(Integer guestId) throws NotFoundException;

    List<Reservation> getAllRestaurantReservations(Integer restaurant) throws NotFoundException;

    void deleteReservation(Integer reservationId);

    Reservation makeReservation(Reservation reservation) throws NotFoundException, DuplicateReservationException;
}
