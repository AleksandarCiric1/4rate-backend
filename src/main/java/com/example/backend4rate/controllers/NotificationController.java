package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.models.dto.Notification;
import com.example.backend4rate.services.impl.NotificationService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

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