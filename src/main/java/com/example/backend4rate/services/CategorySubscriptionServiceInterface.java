package com.example.backend4rate.services;

import com.example.backend4rate.models.entities.CategoryEntity;
import java.util.List;

public interface CategorySubscriptionServiceInterface {

    boolean subscribeToCategory(Integer guestId, Integer categoryId);

    boolean unsubscribeFromCategory(Integer guestId, Integer categoryId);

    boolean isAlreadySubscribed(Integer guestId, Integer categoryId);

    List<CategoryEntity> getSubscribedCategories(Integer guestId);
}
