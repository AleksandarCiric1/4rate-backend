package com.example.backend4rate.services.impl;

import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.CategorySubscription;
import com.example.backend4rate.models.entities.CategorySubscriptionEntity;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.CategorySubscriptionRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.services.CategorySubscriptionServiceInterface;

@Service
public class CategorySubscriptionService implements CategorySubscriptionServiceInterface{

    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final GuestRepository guestRepository;
    public CategorySubscriptionService(CategorySubscriptionRepository categorySubscriptionRepository, CategoryRepository categoryRepository, GuestRepository guestRepository){
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.categoryRepository=categoryRepository;
        this.guestRepository=guestRepository;
    }

    @Override
    public void subscribeToCategory(CategorySubscription categorySubscription) throws NotFoundException {
        CategorySubscriptionEntity categorySubscriptionEntity= new CategorySubscriptionEntity();
        categorySubscriptionEntity.setCategory(categoryRepository.findById(categorySubscription.getCategoryId())
        .orElseThrow(() -> new NotFoundException(CategorySubscriptionService.class.getName())));
        categorySubscriptionEntity.setGuest(guestRepository.findByUserAccount_Id(categorySubscription.getUserAccountId())
        .orElseThrow(() -> new NotFoundException(CategorySubscriptionService.class.getName())));
        categorySubscriptionEntity.setId(null);
        categorySubscriptionRepository.saveAndFlush(categorySubscriptionEntity);
                
    }

}
