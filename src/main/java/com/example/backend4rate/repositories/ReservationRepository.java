package com.example.backend4rate.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer>{

    Optional<ReservationEntity> findByGuestAndRestaurantAndDate(GuestEntity g, RestaurantEntity r, Date d);
    List<ReservationEntity> findAllByGuest_Id(Integer guestId);    
    List<ReservationEntity> findAllByRestaurant_Id(Integer restaurantId);    

}
