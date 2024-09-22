package com.example.backend4rate.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Notification;
import com.example.backend4rate.models.entities.NotificationEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.NotificationRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.NotificationServiceInterface;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class NotificationService implements NotificationServiceInterface {
        private final NotificationRepository notificationRepository;
        private final UserAccountRepository userAccountRepository;
        private final ModelMapper modelMapper;

        public NotificationService(NotificationRepository notificationRepository,
                        UserAccountRepository userAccountRepository, ModelMapper modelMapper) {
                this.notificationRepository = notificationRepository;
                this.userAccountRepository = userAccountRepository;
                this.modelMapper = modelMapper;
        }

        private final Sinks.Many<Notification> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

        private void pushNotification(Notification newNotification) {
                notificationSink
                                .tryEmitNext(newNotification);
        }

        public Flux<ServerSentEvent<Notification>> getReservationNotificationsByUserId(Integer userId) {
                return notificationSink.asFlux()
                                .filter(notification -> notification.getUserAccountId().equals(userId))
                                .map(notification -> ServerSentEvent.<Notification>builder()
                                                .event(notification.getNotificationType())
                                                .data(notification)
                                                .build());
        }

        public void createNotification(Notification newNotification) throws NotFoundException {
                UserAccountEntity userAccountEntity = userAccountRepository.findById(newNotification.getUserAccountId())
                                .orElseThrow(() -> new NotFoundException(NotificationService.class.getName()));
                NotificationEntity notification = new NotificationEntity();
                notification.setCreatedAt(new Date());
                notification.setMessage(newNotification.getMessage());
                notification.setUserAccount(userAccountEntity);
                notification.setRead(false);
                notification.setTitle(newNotification.getTitle());
                notification.setNotificationType(newNotification.getNotificationType());
                notificationRepository.save(notification);
                pushNotification(newNotification);
        }

        public List<Notification> getUserNotifications(Integer userId) {
                List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserAccount_IdAndIsRead(
                                userId,
                                false);
                return notificationEntities.stream().map((elem) -> modelMapper.map(elem, Notification.class))
                                .collect(Collectors.toList());
        }

        public List<Notification> getUnreadNotifications(Integer userId) {
                List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserAccount_IdAndIsRead(
                                userId,
                                false);
                return notificationEntities.stream().map((elem) -> modelMapper.map(elem, Notification.class))
                                .collect(Collectors.toList());
        }

        public void markAsRead(Integer userId) {
                List<NotificationEntity> notifications = notificationRepository.findAllByUserAccount_IdAndIsRead(userId,
                                false);
                for (NotificationEntity notification : notifications) {
                        notification.setRead(true);
                        notificationRepository.save(notification);
                }
        }
}
