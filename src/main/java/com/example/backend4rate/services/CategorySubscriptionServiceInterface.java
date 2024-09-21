package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.CategorySubscription;

public interface CategorySubscriptionServiceInterface {

    void subscribeToCategory(CategorySubscription categorySubscription) throws NotFoundException;

}
