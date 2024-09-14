package com.example.backend4rate.services;

import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.exceptions.InvalidPhoneNumberException;
import com.example.backend4rate.exceptions.DuplicatePhoneNumberException;

import java.util.List;

public interface RestaurantServiceInterface {
    List<Restaurant> getAllRestaurants();  
    Restaurant getRestaurant(Integer restaurantId);
    List<Restaurant> searchRestaurant(String name);  
    boolean updateRestaurantInformation(Restaurant restaurant) throws InvalidPhoneNumberException, DuplicatePhoneNumberException;
    Restaurant addRestaurant(Restaurant restaurant) throws InvalidPhoneNumberException, DuplicatePhoneNumberException;
    boolean addFavoriteRestaurant(Integer restaurantId, Integer guestId);  
    boolean removeFavoriteRestaurant(Integer restaurantId, Integer guestId);  
    List<Restaurant> getFavoriteRestaurants(Integer guestId); 
    List<Restaurant> getRestaurantsByCategories(List<Integer> categoryIds);
 
}
