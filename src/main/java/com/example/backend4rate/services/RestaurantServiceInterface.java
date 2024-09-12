package com.example.backend4rate.services;

import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.CategoryEntity;

import java.util.List;

public interface RestaurantServiceInterface {
    List<Restaurant> searchRestaurant(String name);
    Restaurant getRestaurant(int idRestaurant);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> getFavoriteRestaurants();
    boolean addFavoriteRestaurant(int idRestaurant);
    boolean updateRestaurantInformation(Restaurant restaurant);
    boolean removeFavoriteRestaurant(int idRestaurant);
    List<Restaurant> getAllRestaurants(List<CategoryEntity> categories);
}
