package com.example.backend4rate.services;

import com.example.backend4rate.models.dto.Restaurant;
import java.util.List;

public interface RestaurantServiceInterface {
    List<Restaurant> getAllRestaurants();  
    Restaurant getRestaurant(Integer restaurantId); 
    List<Restaurant> searchRestaurant(String name);  
    boolean updateRestaurantInformation(Restaurant restaurant);  
    Restaurant addRestaurant(Restaurant restaurant);  
    boolean addFavoriteRestaurant(Integer restaurantId, Integer guestId);  
    boolean removeFavoriteRestaurant(Integer restaurantId, Integer guestId);  
    List<Restaurant> getFavoriteRestaurants(Integer guestId); 
    List<Restaurant> getRestaurantsByCategories(List<Integer> categoryIds);
}
