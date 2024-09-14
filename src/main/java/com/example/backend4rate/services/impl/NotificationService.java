package com.example.backend4rate.services.impl;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.entities.NotificationEntity;
import com.example.backend4rate.repositories.NotificationRepository;
import com.example.backend4rate.services.NotificationServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements NotificationServiceInterface {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationEntity> getNotificationsForGuest(Integer guestId) {
        return notificationRepository.findByGuestId(guestId);
    }

    @Override
    public void markAsRead(Integer notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotFoundException("Notification not found"));
        
        notification.setStatus(true);  // Oznaka pročitano
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Integer guestId) {
        List<NotificationEntity> notifications = notificationRepository.findByGuestIdAndStatus(guestId, false);
        for (NotificationEntity notification : notifications) {
            notification.setStatus(true);
            notificationRepository.save(notification);
        }
    }
    
    @Override
    public void saveNotification(NotificationEntity notification) {
        notificationRepository.save(notification);
    }
}
