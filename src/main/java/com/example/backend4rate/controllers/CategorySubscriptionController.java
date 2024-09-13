package com.example.backend4rate.controllers;

import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.services.CategorySubscriptionServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category-subscription")
public class CategorySubscriptionController {

    private final CategorySubscriptionServiceInterface categorySubscriptionService;

    public CategorySubscriptionController(CategorySubscriptionServiceInterface categorySubscriptionService) {
        this.categorySubscriptionService = categorySubscriptionService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToCategory(@RequestParam Integer guestId, @RequestParam Integer categoryId) {
        if (categorySubscriptionService.isAlreadySubscribed(guestId, categoryId)) {
            return ResponseEntity.badRequest().body("Already subscribed");
        }
        categorySubscriptionService.subscribeToCategory(guestId, categoryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribeFromCategory(@RequestParam Integer guestId, @RequestParam Integer categoryId) {
        categorySubscriptionService.unsubscribeFromCategory(guestId, categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscribed-categories/{guestId}")
    public List<CategoryEntity> getSubscribedCategories(@PathVariable Integer guestId) {
        return categorySubscriptionService.getSubscribedCategories(guestId);
    }
}
