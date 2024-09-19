package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.models.dto.RestaurantsPerMonth;
import com.example.backend4rate.models.dto.UsersPerMonth;
import com.example.backend4rate.services.AnalyticServiceInterface;

@RestController
@RequestMapping("/v1/analytics")
public class AnalyticController {

    private final AnalyticServiceInterface analyticService;

    public AnalyticController(AnalyticServiceInterface analyticServiceInterface) {
        this.analyticService = analyticServiceInterface;
    }

    @GetMapping("/user-stats")
    public ResponseEntity<?> getUserCreationStats() {
        List<UsersPerMonth> userStats = analyticService.getUserCreationStatsForLastYear();
        return ResponseEntity.ok(userStats);
    }

    @GetMapping("/restaurant-stats")
    public ResponseEntity<?> getRestaurantCreationStats() {
        List<RestaurantsPerMonth> userStats = analyticService.getRestaurantCreationStatsForLastYear();
        return ResponseEntity.ok(userStats);
    }
}
