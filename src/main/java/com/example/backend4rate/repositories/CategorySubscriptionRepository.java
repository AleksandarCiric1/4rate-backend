package com.example.backend4rate.repositories;

import com.example.backend4rate.models.entities.CategorySubscriptionEntity;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySubscriptionRepository extends JpaRepository<CategorySubscriptionEntity, Integer> {
    List<CategorySubscriptionEntity> findByCategory(CategoryEntity category);
    CategorySubscriptionEntity findByGuestIdAndCategoryId(GuestEntity guest, CategoryEntity category);
    Optional<CategorySubscriptionEntity> findByGuestIdAndCategoryId(Integer guestId, Integer categoryId);
}

