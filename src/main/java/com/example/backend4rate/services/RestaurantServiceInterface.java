package com.example.backend4rate.services;

import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

import java.util.List;

public interface RestaurantServiceInterface {
    List<RestaurantEntity> searchRestaurant(String name);
    RestaurantEntity getRestaurant(int idRestaurant);
    List<RestaurantEntity> getAllRestaurants();
    List<RestaurantEntity> getFavoriteRestaurants();
    boolean addFavoriteRestaurant(int idRestaurant);
    boolean updateRestaurantInformation(RestaurantEntity restaurant);
    boolean removeFavoriteRestaurant(int idRestaurant);
    List<RestaurantEntity> getAllRestaurantsByCategory(List<CategoryEntity> categories);
}
