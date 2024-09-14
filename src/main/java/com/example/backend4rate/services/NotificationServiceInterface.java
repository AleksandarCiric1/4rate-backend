package com.example.backend4rate.services;

import com.example.backend4rate.models.entities.NotificationEntity;
import java.util.List;

public interface NotificationServiceInterface {

    List<NotificationEntity> getNotificationsForGuest(Integer guestId);
    void markAsRead(Integer notificationId);
    void markAllAsRead(Integer guestId);
    void saveNotification(NotificationEntity notification);
}
