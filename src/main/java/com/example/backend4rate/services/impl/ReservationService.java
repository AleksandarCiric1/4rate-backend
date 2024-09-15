package com.example.backend4rate.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.ReservationRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.ReservationServiceInterface;

import jakarta.transaction.Transactional;

@Service
public class ReservationService implements ReservationServiceInterface{
    ReservationRepository reservationRepository;

    ModelMapper modelMapper;

    private GuestRepository guestRepository;
    private RestaurantRepository restaurantRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, RestaurantRepository restaurantRepository, ModelMapper modelMapper){
        this.reservationRepository=reservationRepository;
        this.modelMapper=modelMapper;
        this.guestRepository=guestRepository;
        this.restaurantRepository=restaurantRepository;
    }



    @Override
    public Reservation getReservation(Integer reservationId) throws NotFoundException {
        return modelMapper.map(reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new), Reservation.class);
    }

    @Override
    public void deleteReservation(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public Reservation makeReservation(Reservation reservation) throws NotFoundException, DuplicateReservationException {
        GuestEntity guestEntity= guestRepository.findById(reservation.getGuestId()).orElseThrow(NotFoundException::new );
        RestaurantEntity restaurantEntity= restaurantRepository.findById(reservation.getRestaurantId()).orElseThrow(NotFoundException::new );
        if(reservationRepository.findByGuestAndRestaurantAndDate(guestEntity, restaurantEntity, reservation.getDate()).isPresent())
            throw new DuplicateReservationException("Reservation on this date is already made!");
        ReservationEntity reservationEntity = modelMapper.map(reservation, ReservationEntity.class);
        reservationEntity.setId(null);
        reservationEntity = reservationRepository.saveAndFlush(reservationEntity);

        return modelMapper.map(reservationEntity, Reservation.class);
}



    @Override
    public List<Reservation> getAllGuestReservations(Integer guestId) throws NotFoundException {
       List<ReservationEntity> reservationEntityList = reservationRepository.findAllByGuest_Id(guestId);
        if(reservationEntityList.isEmpty()) throw new NotFoundException("Guest hasn't made any reservations! ");
        return reservationEntityList.stream().map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }



    @Override
    public List<Reservation> getAllRestaurantReservations(Integer restaurantId) throws NotFoundException {
        List<ReservationEntity> reservationEntityList = reservationRepository.findAllByRestaurant_Id(restaurantId);
        if(reservationEntityList.isEmpty()) throw new NotFoundException("There are no reservations for this restaurant!");
        return reservationEntityList.stream().map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        reservationRepository.deleteByDateBefore(now);
    }

}
