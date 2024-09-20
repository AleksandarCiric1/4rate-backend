package com.example.backend4rate.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.ReservationAvailabilityEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

@Repository
public interface ReservationAvailabilityRepository extends JpaRepository<ReservationAvailabilityEntity, Integer>{

    List<ReservationAvailabilityEntity> findByRestaurantAndReservationDateAndTimeSloth(RestaurantEntity restaurant, Date reservationDate, Integer timeSloth);

    
}
