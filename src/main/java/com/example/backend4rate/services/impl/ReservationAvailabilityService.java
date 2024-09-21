package com.example.backend4rate.services.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import com.example.backend4rate.models.entities.ReservationAvailabilityEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.ReservationAvailabilityRepository;
import com.example.backend4rate.services.ReservationAvailabilityServiceInterface;

@Service
public class ReservationAvailabilityService implements ReservationAvailabilityServiceInterface {

    ReservationAvailabilityRepository reservationAvailabilityRepository;

    public ReservationAvailabilityService(ReservationAvailabilityRepository reservationAvailabilityRepository){
        this.reservationAvailabilityRepository=reservationAvailabilityRepository;    
    }

    @Override
    public void createReservationAvailability(ReservationEntity reservationEntity) {
        ReservationAvailabilityEntity reservationAvailabilityEntity = new ReservationAvailabilityEntity();
        reservationAvailabilityEntity.setRestaurant(reservationEntity.getRestaurant());
        reservationAvailabilityEntity.setReservationDate(reservationEntity.getDate());
        reservationAvailabilityEntity.setTimeSloth(reservationEntity.getTimeSloth());
        reservationAvailabilityEntity.setId(null);
        reservationAvailabilityRepository.saveAndFlush(reservationAvailabilityEntity);

    }

    @Override
    public boolean deleteReservationAvailability(ReservationEntity reservationEntity) {
        return reservationAvailabilityRepository.deleteOneByValue(reservationEntity.getRestaurant().getId(),
         reservationEntity.getDate(), reservationEntity.getTimeSloth());
    }

    @Override
    public boolean isAvailable(ReservationEntity reservationEntity){
        Integer capacity = reservationEntity.getRestaurant().getCapacity();
        RestaurantEntity restaurantEntity = reservationEntity.getRestaurant();
        Integer timeSloth = reservationEntity.getTimeSloth();
        Date reservationDate = reservationEntity.getDate();
        Long usedCapacity = reservationAvailabilityRepository.findByRestaurantAndReservationDateAndTimeSloth(restaurantEntity, reservationDate, timeSloth).stream().count();
        if (usedCapacity < (long)capacity) 
            return true;
        else return false;
    }

  
    @Scheduled(cron = "0 0 4 * * ?")
    public void freeReservationSpace() {
        reservationAvailabilityRepository.deleteByReservationDateBeforeToday(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }


}
