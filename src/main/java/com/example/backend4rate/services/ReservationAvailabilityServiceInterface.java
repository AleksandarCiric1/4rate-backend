package com.example.backend4rate.services;

import com.example.backend4rate.models.entities.ReservationEntity;

public interface ReservationAvailabilityServiceInterface {

       public void createReservationAvailability(ReservationEntity reservationEntity);
       public boolean isAvailable(ReservationEntity reservationEntity);

}
