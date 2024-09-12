package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.dto.RestaurantBlock;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

import java.util.List;

public interface RestaurantServiceInterface {
    List<Restaurant> getAll();
    boolean blockRestaurant(RestaurantBlock restaurantTBlock) throws NotFoundException;
    // List<RestaurantEntity> searchRestaurant(String name);
    // RestaurantEntity getRestaurant(int idRestaurant);
    // List<RestaurantEntity> getFavoriteRestaurants();
    // boolean addFavoriteRestaurant(int idRestaurant);
    // boolean updateRestaurantInformation(RestaurantEntity restaurant);
    // boolean removeFavoriteRestaurant(int idRestaurant);
    // List<RestaurantEntity> getAllRestaurantsByCategory(List<CategoryEntity> categories);
}
