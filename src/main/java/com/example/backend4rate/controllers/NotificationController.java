package com.example.backend4rate.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Notification;
import com.example.backend4rate.models.dto.NotificationDTO;
import com.example.backend4rate.services.impl.NotificationService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Integer userId) {
        return notificationService.getUserNotifications(userId);
    }

    @GetMapping("/unread/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Integer userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    @PostMapping("/read/{userId}")
    public void markAsRead(@PathVariable Integer userId) {
        notificationService.markAsRead(userId);
    }

    @GetMapping("/stream/{userId}")
    public Flux<ServerSentEvent<Notification>> streamReservationApproval(@PathVariable Integer userId) {
        return notificationService.getReservationNotificationsByUserId(userId);
    }

}