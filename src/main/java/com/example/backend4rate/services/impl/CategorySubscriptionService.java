package com.example.backend4rate.services.impl;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.CategorySubscriptionEntity;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.repositories.CategorySubscriptionRepository;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.services.CategorySubscriptionServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategorySubscriptionService implements CategorySubscriptionServiceInterface {

    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final GuestRepository guestRepository;
    private final CategoryRepository categoryRepository;

    public CategorySubscriptionService(CategorySubscriptionRepository categorySubscriptionRepository, 
                                       GuestRepository guestRepository, 
                                       CategoryRepository categoryRepository) {
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.guestRepository = guestRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean subscribeToCategory(Integer guestId, Integer categoryId) {
        GuestEntity guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new NotFoundException("Guest not found with id: " + guestId));
        CategoryEntity category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));

        Optional<CategorySubscriptionEntity> subscriptionOpt = categorySubscriptionRepository.findByGuestIdAndCategoryId(guestId, categoryId);

        if (subscriptionOpt.isPresent()) {
            CategorySubscriptionEntity subscription = subscriptionOpt.get();
            if (!subscription.isSubscribed()) {
                subscription.setSubscribed(true);
                categorySubscriptionRepository.save(subscription);
            }
        } else {
            CategorySubscriptionEntity newSubscription = new CategorySubscriptionEntity();
            newSubscription.setGuest(guest);
            newSubscription.setCategory(category);
            categorySubscriptionRepository.save(newSubscription);
        }
        return true;
    }

    @Override
    public boolean unsubscribeFromCategory(Integer guestId, Integer categoryId) {
        CategorySubscriptionEntity subscription = categorySubscriptionRepository.findByGuestIdAndCategoryId(guestId, categoryId)
            .orElseThrow(() -> new NotFoundException("Subscription not found"));

        subscription.setSubscribed(false);
        categorySubscriptionRepository.save(subscription);
        return true;
    }

    @Override
    public boolean isAlreadySubscribed(Integer guestId, Integer categoryId) {
        return categorySubscriptionRepository.findByGuestIdAndCategoryId(guestId, categoryId)
            .map(CategorySubscriptionEntity::isSubscribed)
            .orElse(false);
    }

    @Override
    public List<CategoryEntity> getSubscribedCategories(Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new NotFoundException("Guest not found with id: " + guestId));

        return guest.getCategorySubscriptions().stream()
            .map(CategorySubscriptionEntity::getCategory)
            .collect(Collectors.toList());
    }
}
