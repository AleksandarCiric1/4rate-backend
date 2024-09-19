package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.models.dto.RestaurantsPerMonth;
import com.example.backend4rate.models.dto.UsersPerMonth;

public interface AnalyticServiceInterface {
    List<UsersPerMonth> getUserCreationStatsForLastYear();

    List<RestaurantsPerMonth> getRestaurantCreationStatsForLastYear();
}
