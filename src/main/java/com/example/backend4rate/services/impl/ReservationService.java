package com.example.backend4rate.services.impl;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.DuplicateReservationException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.ReservationsFullException;
import com.example.backend4rate.models.dto.Notification;
import com.example.backend4rate.models.dto.Reservation;
import com.example.backend4rate.models.dto.ReservationRequest;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ReservationEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.models.enums.NotificationType;
import com.example.backend4rate.models.enums.ReservationStatus;
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
        private final NotificationService notificationService;

        public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository,
                        RestaurantRepository restaurantRepository, ModelMapper modelMapper,
                        RestaurantPhoneRepository restaurantPhoneRepository, CategoryRepository categoryRepository,
                        RestaurantCategoryRepository restaurantCategoryRepository,
                        ReservationAvailabilityService reservationAvailabilityService,
                        NotificationService notificationService) {
                this.reservationRepository = reservationRepository;
                this.modelMapper = modelMapper;
                this.guestRepository = guestRepository;
                this.restaurantRepository = restaurantRepository;
                this.reservationAvailabilityService = reservationAvailabilityService;
                this.notificationService = notificationService;
        }

        @Override
        public Reservation getReservation(Integer reservationId) throws NotFoundException {
                return modelMapper.map(
                                reservationRepository.findById(reservationId)
                                                .orElseThrow(() -> new NotFoundException(
                                                                ReservationService.class.getName())),
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
                if (reservationRepository
                                .findByGuestAndRestaurantAndDate(guestEntity, restaurantEntity, reservation.getDate())
                                .isPresent())
                        throw new DuplicateReservationException("Reservation on this date is already made!");

                reservationEntity.setGuest(guestEntity);
                reservationEntity.setRestaurant(restaurantEntity);
                reservationEntity.setCreatedAt(new Date());
                reservationEntity.setTimeSloth(reservation.getTime().toLocalTime().getHour());

                reservationEntity.setId(null);
                reservationEntity.setStatus(ReservationStatus.PENDING.name().toLowerCase());
                reservationEntity = reservationRepository.saveAndFlush(reservationEntity);
                notificationService
                                .createNotification(new Notification(
                                                restaurantEntity.getManager().getUserAccount().getId(),
                                                "You have new reservation for " + reservationEntity.getDate()
                                                                + " in restaurant "
                                                                + reservationEntity.getRestaurant().getName() + ".",
                                                "New reservation", NotificationType.NOTIFICATION_MANAGER.getValue()));
                return modelMapper.map(reservationEntity, Reservation.class);

        }

        @Override
        public List<Reservation> getAllGuestReservations(Integer userAccountId) throws NotFoundException {
                GuestEntity guestEntity = guestRepository.findByUserAccount_Id(userAccountId)
                                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
                List<ReservationEntity> reservationEntityList = reservationRepository
                                .findAllByGuest_Id(guestEntity.getId());
                if (reservationEntityList.isEmpty())
                        throw new NotFoundException("Guest hasn't made any reservations! ");
                return reservationEntityList.stream()
                                .filter(l -> (l.getStatus().equals(ReservationStatus.PENDING.name().toLowerCase())
                                                || l.getStatus().equals(
                                                                ReservationStatus.APPROVED.name().toLowerCase()))
                                                && l.getDate().after(getDayBeforeToday()))
                                .map(l -> modelMapper.map(l, Reservation.class))
                                .collect(Collectors.toList());
        }

        private Date getDayBeforeToday() {
                LocalDate localDate = LocalDate.now().minusDays(1);
                Date dayBefor = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                return dayBefor;
        }

        @Override
        public List<Reservation> getAllRestaurantReservations(Integer restaurantId) throws NotFoundException {
                List<ReservationEntity> reservationEntityList = reservationRepository
                                .findAllByRestaurant_Id(restaurantId);
                if (reservationEntityList.isEmpty())
                        throw new NotFoundException("There are no reservations for this restaurant!");
                return reservationEntityList.stream()
                                .filter(l -> !l.getStatus().equals(ReservationStatus.DENIED.name().toLowerCase())
                                                && l.getDate().after(getDayBeforeToday()))
                                .map(l -> modelMapper.map(l, Reservation.class))
                                .collect(Collectors.toList());
        }

        @Override
        public Reservation approveReservation(Integer reservationId)
                        throws NotFoundException, ReservationsFullException {
                ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
                if (reservationAvailabilityService.isAvailable(reservationEntity)) {
                        reservationEntity.setStatus(ReservationStatus.APPROVED.name().toLowerCase());
                        reservationRepository.saveAndFlush(reservationEntity);
                        reservationAvailabilityService.createReservationAvailability(reservationEntity);
                        notificationService
                                        .createNotification(new Notification(
                                                        reservationEntity.getGuest().getUserAccount().getId(),
                                                        "Your reservation for " + reservationEntity.getDate()
                                                                        + " in restaurant "
                                                                        + reservationEntity.getRestaurant().getName()
                                                                        + " has been approved!",
                                                        "Reservation approval",
                                                        NotificationType.NOTIFICATION_GUEST.getValue()));
                        return modelMapper.map(reservationEntity, Reservation.class);
                } else
                        throw new ReservationsFullException("This appointment is unavailable! ");

        }

        @Override
        public Reservation denyReservation(Integer reservationId) throws NotFoundException {
                ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
                reservationEntity.setStatus(ReservationStatus.DENIED.name().toLowerCase());
                reservationRepository.saveAndFlush(reservationEntity);
                notificationService.createNotification(new Notification(
                                reservationEntity.getGuest().getUserAccount().getId(),
                                "Your reservation for " + reservationEntity.getDate() + " in restaurant "
                                                + reservationEntity.getRestaurant().getName() + " has been denied!",
                                "Reservation denial", NotificationType.NOTIFICATION_GUEST.getValue()));
                return modelMapper.map(reservationEntity, Reservation.class);
        }

        @Override
        public Reservation cancelReservation(Integer reservationId) throws NotFoundException {
                ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                                .orElseThrow(() -> new NotFoundException(ReservationService.class.getName()));
                if (ReservationStatus.APPROVED.name().toLowerCase().equals(reservationEntity.getStatus())) {
                        reservationEntity.setStatus(ReservationStatus.CANCELED.name().toLowerCase());
                        reservationRepository.saveAndFlush(reservationEntity);
                        reservationAvailabilityService.deleteReservationAvailability(reservationEntity);
                }

                RestaurantEntity restaurantEntity = reservationEntity.getRestaurant();
                UserAccountEntity guest = reservationEntity.getGuest().getUserAccount();
                notificationService
                                .createNotification(new Notification(
                                                restaurantEntity.getManager().getUserAccount().getId(),
                                                guest.getFirstName() + " " + guest.getLastName()
                                                                + " canceled his reservation for "
                                                                + reservationEntity.getDate() + " in your restaurant.",
                                                "Cancelation of reservation",
                                                NotificationType.NOTIFICATION_MANAGER.getValue()));
                return modelMapper.map(reservationEntity, Reservation.class);

        }

        @Override
        public List<Reservation> getAllRestaurantReservationsByDate(Integer restaurantId, Date reservationDate)
                        throws NotFoundException {
                List<ReservationEntity> reservationEntityList = reservationRepository
                                .findAllByRestaurant_Id(restaurantId);
                if (reservationEntityList.isEmpty())
                        throw new NotFoundException("There are no reservations for this date!");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return reservationEntityList.stream()
                                .filter(l -> !l.getStatus().equals(ReservationStatus.DENIED.name().toLowerCase())
                                                && sdf.format(l.getDate()).compareTo(sdf.format(reservationDate)) == 0)
                                .map(l -> modelMapper.map(l, Reservation.class))
                                .collect(Collectors.toList());
        }

        private ReservationEntity changeStatusToDenyReservation(ReservationEntity reservationEntity) {
                reservationEntity.setStatus(ReservationStatus.DENIED.name().toLowerCase());
                return reservationEntity;
        }

        @Scheduled(fixedRate = 900000)
        public void expireReservation() throws NotFoundException {
                Date today = new Date();
                Time currentTime30 = Time.valueOf(LocalTime.now().plusMinutes(30));
                List<ReservationEntity> listOfExpireReservation = reservationRepository.findAllByDateAndTimeBefore(
                                today,
                                currentTime30);
                System.out.println(listOfExpireReservation.get(0).getId());

                for (ReservationEntity reservationEntity : listOfExpireReservation) {
                        if ("pending".equals(reservationEntity.getStatus())) {
                                reservationEntity = this.changeStatusToDenyReservation(reservationEntity);
                        }
                        System.out.println(reservationEntity.getId());
                }
                reservationRepository.saveAllAndFlush(listOfExpireReservation);

        }


    private ReservationEntity changeStatusToDenyReservation(ReservationEntity reservationEntity){
        reservationEntity.setStatus(ReservationStatus.DENIED.name().toLowerCase());
        // TODO Obavijesti gosta o rezultatu obrade
        return reservationEntity;
    }
    @Scheduled(fixedRate = 900000)
    public void expireReservation() throws NotFoundException{
        Date today = new Date();
        Time currentTime30 = Time.valueOf(LocalTime.now().plusMinutes(30));
        List<ReservationEntity> listOfExpireReservation = reservationRepository.findAllByDateAndTimeBefore(today, currentTime30);
        System.out.println(listOfExpireReservation.get(0).getId());

        for(ReservationEntity reservationEntity : listOfExpireReservation){
            if("pending".equals(reservationEntity.getStatus())){
                reservationEntity = this.changeStatusToDenyReservation(reservationEntity);
            }
            System.out.println(reservationEntity.getId());
        }            reservationRepository.saveAllAndFlush(listOfExpireReservation);

    }

}
