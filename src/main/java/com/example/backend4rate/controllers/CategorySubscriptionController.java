package com.example.backend4rate.controllers;

import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.services.CategorySubscriptionServiceInterface;
import com.example.backend4rate.exceptions.CategoryNotFoundException;
import com.example.backend4rate.exceptions.GuestNotFoundException;
import com.example.backend4rate.exceptions.SubscriptionNotFoundException;
import org.springframework.http.HttpStatus;
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
        try {
            if (categorySubscriptionService.isAlreadySubscribed(guestId, categoryId)) {
                return ResponseEntity.badRequest().body("Already subscribed");
            }
            categorySubscriptionService.subscribeToCategory(guestId, categoryId);
            return ResponseEntity.ok().build();
        } catch (GuestNotFoundException | CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribeFromCategory(@RequestParam Integer guestId, @RequestParam Integer categoryId) {
        try {
            categorySubscriptionService.unsubscribeFromCategory(guestId, categoryId);
            return ResponseEntity.ok().build();
        } catch (SubscriptionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/subscribed-categories/{guestId}")
    public ResponseEntity<?> getSubscribedCategories(@PathVariable Integer guestId) {
        try {
            List<CategoryEntity> subscribedCategories = categorySubscriptionService.getSubscribedCategories(guestId);
            return ResponseEntity.ok(subscribedCategories);
        } catch (GuestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
