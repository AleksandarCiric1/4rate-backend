package com.example.backend4rate.services.impl;

import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<RestaurantEntity> searchRestaurant(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public RestaurantEntity getRestaurant(int idRestaurant) {
        return restaurantRepository.findById(idRestaurant)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @Override
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<RestaurantEntity> getFavoriteRestaurants() {
        return restaurantRepository.findByIsFavoriteTrue();
    }

    @Override
    public boolean addFavoriteRestaurant(int idRestaurant) {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(idRestaurant);
        if (restaurant.isPresent()) {
            RestaurantEntity entity = restaurant.get();
            entity.setFavorite(true);
            restaurantRepository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRestaurantInformation(RestaurantEntity restaurant) {
        if (restaurantRepository.existsById(restaurant.getId())) {
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFavoriteRestaurant(int idRestaurant) {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(idRestaurant);
        if (restaurant.isPresent()) {
            RestaurantEntity entity = restaurant.get();
            entity.setFavorite(false);
            restaurantRepository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public List<RestaurantEntity> getAllRestaurantsByCategory(List<CategoryEntity> categories) {
        return restaurantRepository.findByRestaurantCategoriesCategoryIn(categories);
    }
}

