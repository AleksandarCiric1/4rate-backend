package com.example.backend4rate.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import com.example.backend4rate.models.dto.Notification;
import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.ReservationsFullException;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.ReservationRequest;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.enums.ReservationStatus;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.ReservationRepository;
import com.example.backend4rate.repositories.RestaurantCategoryRepository;
import com.example.backend4rate.repositories.RestaurantPhoneRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.ReservationServiceInterface;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

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
        return modelMapper.map(reservationRepository.findById(reservationId).orElseThrow(() -> new NotFoundException(ReservationService.class.getName())),
                Reservation.class);
    }

    @Override
    public Reservation makeReservation(ReservationRequest reservation)
            throws NotFoundException, DuplicateReservationException, ReservationsFullException {
        ReservationEntity reservationEntity = modelMapper.map(reservation, ReservationEntity.class);
        GuestEntity guestEntity = guestRepository.findByUserAccount_Id(reservation.getUserAccountId())
                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        RestaurantEntity restaurantEntity = restaurantRepository.findById(reservation.getRestaurantId())
                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        if (reservationRepository.findByGuestAndRestaurantAndDate(guestEntity, restaurantEntity, reservation.getDate())
                .isPresent())
            throw new DuplicateReservationException("Reservation on this date is already made!");

        reservationEntity.setGuest(guestEntity);
        reservationEntity.setRestaurant(restaurantEntity);
        reservationEntity.setCreatedAt(new Date());
        reservationEntity.setTimeSloth(reservation.getTime().toLocalTime().getHour());

        reservationEntity.setId(null);
        reservationEntity.setStatus(ReservationStatus.PENDING.name().toLowerCase());
        reservationEntity = reservationRepository.saveAndFlush(reservationEntity);
        return modelMapper.map(reservationEntity, Reservation.class);

    }

    @Override
    public List<Reservation> getAllGuestReservations(Integer userAccountId) throws NotFoundException {
        GuestEntity guestEntity = guestRepository.findByUserAccount_Id(userAccountId).orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        List<ReservationEntity> reservationEntityList = reservationRepository.findAllByGuest_Id(guestEntity.getId());
        if (reservationEntityList.isEmpty())
            throw new NotFoundException("Guest hasn't made any reservations! ");
        return reservationEntityList.stream()
                .filter(l -> (l.getStatus().equals(ReservationStatus.PENDING.name().toLowerCase())
                        || l.getStatus().equals(ReservationStatus.APPROVED.name().toLowerCase()))
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
                .filter(l -> !l.getStatus().equals(ReservationStatus.DENIED.name().toLowerCase())
                        && l.getDate().after(new Date()))
                .map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation approveReservation(Integer reservationId) throws NotFoundException, ReservationsFullException {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        if (reservationAvailabilityService.isAvailable(reservationEntity)) {
            reservationEntity.setStatus(ReservationStatus.APPROVED.name().toLowerCase());
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
                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        reservationEntity.setStatus(ReservationStatus.DENIED.name().toLowerCase());
        reservationRepository.saveAndFlush(reservationEntity);
        // TODO Obavijesti gosta o rezultatu obrade
        return modelMapper.map(reservationEntity, Reservation.class);
    }

    @Override
    public Reservation cancelReservation(Integer reservationId) throws NotFoundException {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
        if(ReservationStatus.APPROVED.name().toLowerCase().equals(reservationEntity.getStatus()))
        {
            reservationEntity.setStatus(ReservationStatus.CANCELED.name().toLowerCase());
            reservationRepository.saveAndFlush(reservationEntity);
            reservationAvailabilityService.deleteReservationAvailability(reservationEntity);
        }

        // TODO Obavijesti gosta o rezultatu obrade
        return modelMapper.map(reservationEntity, Reservation.class);

    }

    @Override
    public List<Reservation> getAllRestaurantReservationsByDate(Integer restaurantId, Date reservationDate)
            throws NotFoundException {
        List<ReservationEntity> reservationEntityList = reservationRepository.findAllByRestaurant_Id(restaurantId);
        if (reservationEntityList.isEmpty())
            throw new NotFoundException("There are no reservations for this date!");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return reservationEntityList.stream()
                .filter(l -> !l.getStatus().equals(ReservationStatus.DENIED.name().toLowerCase())
                        && sdf.format(l.getDate()).compareTo(sdf.format(reservationDate)) == 0)
                .map(l -> modelMapper.map(l, Reservation.class))
                .collect(Collectors.toList());
    }

    /** NOTIFICATIONS */
    private final Sinks.Many<Notification> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

    // Method to trigger an approval notification event
    public void approveReservation(Integer userId, String reservationId) {
        // Logic for reservation approval...

        // Emit notification when the reservation is approved
        notificationSink
                .tryEmitNext(new Notification(userId, "Your reservation #" + reservationId + " has been approved."));
    }

    // Streaming reservation approval notifications to clients
    public Flux<ServerSentEvent<Notification>> getReservationApprovalsByUserId(Integer userId) {
        return notificationSink.asFlux()
                .filter(notification -> notification.getId().equals(userId)) // Only stream for specific user
                .map(notification -> ServerSentEvent.<Notification>builder()
                        .event("reservation-approval")
                        .data(notification)
                        .build());
    }
}
