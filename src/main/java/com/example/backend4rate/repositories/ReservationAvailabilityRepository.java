package com.example.backend4rate.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.ReservationAvailabilityEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

import jakarta.transaction.Transactional;

@Repository
public interface ReservationAvailabilityRepository extends JpaRepository<ReservationAvailabilityEntity, Integer>{

    List<ReservationAvailabilityEntity> findByRestaurantAndReservationDateAndTimeSloth(RestaurantEntity restaurant, Date reservationDate, Integer timeSloth);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM reservation_availability WHERE restaurant_id = :restaurantId"
    + " AND reservation_date = :reservationDate AND time_sloth = :timeSloth LIMIT 1", nativeQuery = true)
    boolean deleteOneByValue(@Param("restaurantId") Integer restaurantId, 
                            @Param("reservationDate") Date reservationDate, 
                            @Param("timeSloth") Integer timeSloth);

    
    @Modifying
    @Transactional
    @Query( "DELETE FROM ReservationAvailabilityEntity r  WHERE r.reservationDate < :today" )
    void deleteByReservationDateBeforeToday(@Param("today") Date today);


    
}
