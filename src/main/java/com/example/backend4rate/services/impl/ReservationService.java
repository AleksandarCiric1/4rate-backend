package com.example.backend4rate.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.ReservationsFullException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.ReservationRequest;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ReservationAvailabilityEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.ReservationRepository;
import com.example.backend4rate.repositories.RestaurantCategoryRepository;
import com.example.backend4rate.repositories.RestaurantPhoneRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.ReservationServiceInterface;

@Service
public class ReservationService implements ReservationServiceInterface {
    ReservationRepository reservationRepository;

    ModelMapper modelMapper;

    private final GuestRepository guestRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationAvailabilityService reservationAvailabilityService;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository,
            RestaurantRepository restaurantRepository, ModelMapper modelMapper,
            RestaurantPhoneRepository restaurantPhoneRepository, CategoryRepository categoryRepository,
            RestaurantCategoryRepository restaurantCategoryRepository,
            ReservationAvailabilityService reservationAvailabilityService) {
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
        this.guestRepository = guestRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationAvailabilityService = reservationAvailabilityService;
    }

    @Override
    public Reservation getReservation(Integer reservationId) throws NotFoundException {
        return modelMapper.map(reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new),
                Reservation.class);
    }

    @Override
    public Reservation makeReservation(ReservationRequest reservation)
            throws NotFoundException, DuplicateReservationException, ReservationsFullException {
        ReservationEntity reservationEntity = modelMapper.map(reservation, ReservationEntity.class);
        GuestEntity guestEntity = guestRepository.findByUserAccount_Id(reservation.getUserAccountId())
                .orElseThrow(NotFoundException::new);
        RestaurantEntity restaurantEntity = restaurantRepository.findById(reservation.getRestaurantId())
                .orElseThrow(NotFoundException::new);
        if (reservationRepository.findByGuestAndRestaurantAndDate(guestEntity, restaurantEntity, reservation.getDate())
                .isPresent())
            throw new DuplicateReservationException("Reservation on this date is already made!");

        reservationEntity.setGuest(guestEntity);
        reservationEntity.setRestaurant(restaurantEntity);
        reservationEntity.setTimeSloth(reservation.getTime().toLocalTime().getHour());

        reservationEntity.setId(null);
        reservationEntity.setStatus("pending");
        reservationEntity = reservationRepository.saveAndFlush(reservationEntity);
        return modelMapper.map(reservationEntity, Reservation.class);

    }

    @Override
    public List<Reservation> getAllGuestReservations(Integer guestId) throws NotFoundException {
        List<ReservationEntity> reservationEntityList = reservationRepository.findAllByGuest_Id(guestId);
        if (reservationEntityList.isEmpty())
            throw new NotFoundException("Guest hasn't made any reservations! ");
        return reservationEntityList.stream()
                .filter(l -> (l.getStatus().equals("pending") || l.getStatus().equals("approved"))
                        && l.getDate().after(new Date()))
                .map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> getAllRestaurantReservations(Integer restaurantId) throws NotFoundException {
        List<ReservationEntity> reservationEntityList = reservationRepository.findAllByRestaurant_Id(restaurantId);
        if (reservationEntityList.isEmpty())
            throw new NotFoundException("There are no reservations for this restaurant!");
        return reservationEntityList.stream()
                .filter(l -> !l.getStatus().equals("denied") && l.getDate().after(new Date()))
                .map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation approveReservation(Integer reservationId) throws NotFoundException, ReservationsFullException {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(NotFoundException::new);
        if (reservationAvailabilityService.isAvailable(reservationEntity)) {
            reservationEntity.setStatus("approved");
            reservationRepository.saveAndFlush(reservationEntity);
            reservationAvailabilityService.createReservationAvailability(reservationEntity);
            return modelMapper.map(reservationEntity, Reservation.class);
        } else
            throw new ReservationsFullException("This appointment is unavailable! ");
        // TO-DO Obavijesti gosta o rezultatu obrade
    }

    @Override
    public Reservation denyReservation(Integer reservationId) throws NotFoundException {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(NotFoundException::new);
        reservationEntity.setStatus("denied");
        reservationRepository.saveAndFlush(reservationEntity);
        // TO-DO Obavijesti gosta o rezultatu obrade
        return modelMapper.map(reservationEntity, Reservation.class);
    }

    @Override
    public Reservation cancelReservation(Integer reservationId) throws NotFoundException {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(NotFoundException::new);
        reservationEntity.setStatus("canceled");
        reservationRepository.saveAndFlush(reservationEntity);
        // TO-DO Obavijesti gosta o rezultatu obrade
        return modelMapper.map(reservationEntity, Reservation.class);

    }

    @Override
    public Long numberOfReservationsByMonth(Integer restaurantId, Integer month, Integer year) {
        return reservationRepository.countReservationsByRestaurantAndMonthAndYear(restaurantId, month, year);
    }

}
