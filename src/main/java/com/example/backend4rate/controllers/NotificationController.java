package com.example.backend4rate.controllers;

import com.example.backend4rate.models.entities.NotificationEntity;
import com.example.backend4rate.services.impl.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsByGuest(@PathVariable Integer guestId) {
        return ResponseEntity.ok(notificationService.getNotificationsForGuest(guestId));
    }

    @PutMapping("/mark/{notificationId}")
    public ResponseEntity<String> markNotification(@PathVariable Integer notificationId, @RequestParam boolean status) {
        notificationService.markAsReadOrUnread(notificationId, status);
        return ResponseEntity.ok("Notification status updated");
    }
}
