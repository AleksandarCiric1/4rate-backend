package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, GuestRepository guestRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.guestRepository = guestRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantEntity.setId(null);  
        RestaurantEntity savedEntity = restaurantRepository.save(restaurantEntity);
        return modelMapper.map(savedEntity, Restaurant.class);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }

    @Override
    public Restaurant getRestaurant(int idRestaurant) {
        RestaurantEntity restaurant = restaurantRepository.findById(idRestaurant)
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + idRestaurant));
        return modelMapper.map(restaurant, Restaurant.class);
    }

    @Override
    public List<Restaurant> searchRestaurant(String name) {
        return restaurantRepository.findByNameContaining(name).stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean updateRestaurantInformation(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantRepository.save(restaurantEntity);
        return true;
    }

    @Override
    public boolean addFavoriteRestaurant(int idRestaurant, int idGuest) {
        GuestEntity guest = guestRepository.findById(idGuest)
            .orElseThrow(() -> new RuntimeException("Guest not found with id: " + idGuest));
        RestaurantEntity restaurant = restaurantRepository.findById(idRestaurant)
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + idRestaurant));

        guest.getFavoriteRestaurants().add(restaurant);  
        guestRepository.save(guest);  
        return true;
    }

    @Override
    public boolean removeFavoriteRestaurant(int idRestaurant, int idGuest) {
        GuestEntity guest = guestRepository.findById(idGuest)
            .orElseThrow(() -> new RuntimeException("Guest not found with id: " + idGuest));
        RestaurantEntity restaurant = restaurantRepository.findById(idRestaurant)
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + idRestaurant));

        guest.getFavoriteRestaurants().remove(restaurant);  
        guestRepository.save(guest);  
        return true;
    }

    @Override
    public List<Restaurant> getFavoriteRestaurants(int idGuest) {
        GuestEntity guest = guestRepository.findById(idGuest)
            .orElseThrow(() -> new RuntimeException("Guest not found with id: " + idGuest));

        return guest.getFavoriteRestaurants().stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }
}

