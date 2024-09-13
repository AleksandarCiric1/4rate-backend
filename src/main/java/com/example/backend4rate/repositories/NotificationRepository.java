package com.example.backend4rate.repositories;

import com.example.backend4rate.models.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    // Pronađi sve notifikacije za određenog gosta
    List<NotificationEntity> findByGuestId(Integer guestId);

    // Pronađi sve nepročitane notifikacije za određenog gosta
    List<NotificationEntity> findByGuestIdAndStatus(Integer guestId, boolean status);
}
