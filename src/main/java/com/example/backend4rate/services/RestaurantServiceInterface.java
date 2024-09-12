package com.example.backend4rate.services;

import com.example.backend4rate.models.dto.Restaurant;
import java.util.List;

public interface RestaurantServiceInterface {
    List<Restaurant> getAllRestaurants();  
    Restaurant getRestaurant(int idRestaurant); 
    List<Restaurant> searchRestaurant(String name);  
    boolean updateRestaurantInformation(Restaurant restaurant);  
    Restaurant addRestaurant(Restaurant restaurant);  
    boolean addFavoriteRestaurant(int idRestaurant, int idGuest);  
    boolean removeFavoriteRestaurant(int idRestaurant, int idGuest);  
    List<Restaurant> getFavoriteRestaurants(int idGuest); 
}
