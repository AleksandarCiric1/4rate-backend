package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.CategorySubscription;
import com.example.backend4rate.services.impl.CategorySubscriptionService;

@RestController
@RequestMapping("/v1/categorySubscriptions")
public class CategorySubscriptionController {

    private final CategorySubscriptionService categorySubscriptionService;

    public CategorySubscriptionController(CategorySubscriptionService categorySubscriptionService){
        this.categorySubscriptionService=categorySubscriptionService;
    }

    @PostMapping("/subscribeToCategory")
    public void subscribeToCategory(@RequestBody CategorySubscription categorySubscription) throws NotFoundException{
        categorySubscriptionService.subscribeToCategory(categorySubscription);
    }
}
