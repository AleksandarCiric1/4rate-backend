package com.example.backend4rate.services;

import java.util.Date;
import java.util.List;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.ReservationsFullException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.ReservationRequest;

public interface ReservationServiceInterface {

    Reservation getReservation(Integer reservationId) throws NotFoundException;

    List<Reservation> getAllGuestReservations(Integer userAccountId) throws NotFoundException;

    List<Reservation> getAllRestaurantReservations(Integer restaurant) throws NotFoundException;
   
    List<Reservation> getAllRestaurantReservationsByDate(Integer restaurant, Date reservationDate) throws NotFoundException;
    

    Reservation makeReservation(ReservationRequest reservation) throws NotFoundException, DuplicateReservationException, ReservationsFullException;

    Reservation approveReservation(Integer reservationId) throws NotFoundException, ReservationsFullException;

    Reservation denyReservation(Integer reservationId) throws NotFoundException;

    Reservation cancelReservation(Integer reservationId) throws NotFoundException;

    public Long numberOfReservationsByMonth(Integer restaurantId, Integer month, Integer year);

}
