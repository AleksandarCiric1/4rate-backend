package com.example.backend4rate.services;

import java.util.Date;
import java.util.List;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;

public interface ReservationServiceInterface {

    Reservation getReservation(Integer reservationId) throws NotFoundException;

    List<Reservation> getAllGuestReservations(Integer guestId) throws NotFoundException;

    List<Reservation> getAllRestaurantReservations(Integer restaurant) throws NotFoundException;

    //void deleteReservation(Integer reservationId) throws NotFoundException;

    Reservation makeReservation(Reservation reservation) throws NotFoundException, DuplicateReservationException;

    Reservation approveReservation(Integer reservationId) throws NotFoundException;

    Reservation denyReservation(Integer reservationId) throws NotFoundException;

    Reservation cancelReservation(Integer reservationId) throws NotFoundException;

    public Long numberOfReservationsByMonth(Integer restaurantId, Integer month, Integer year);

}
