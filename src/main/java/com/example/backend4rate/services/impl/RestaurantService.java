package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.example.backend4rate.exceptions.NotFoundException;
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

    public RestaurantService(RestaurantRepository restaurantRepository, GuestRepository guestRepository,ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.guestRepository = guestRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantEntity.setId(null);
        RestaurantEntity savedEntity = restaurantRepository.saveAndFlush(restaurantEntity);
        return modelMapper.map(savedEntity, Restaurant.class);
    }


    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }

    @Override
    public Restaurant getRestaurant(Integer restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));
        return modelMapper.map(restaurant, Restaurant.class);
    }

    @Override
    public List<Restaurant> searchRestaurant(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }

    @Override
    public boolean updateRestaurantInformation(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantRepository.saveAndFlush(restaurantEntity);
        return true;
    }

    @Override
    public boolean addFavoriteRestaurant(Integer restaurantId, Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new RuntimeException("Guest not found with id: " + guestId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        guest.getFavoriteRestaurants().add(restaurant);  
        guestRepository.saveAndFlush(guest);  
        return true;
    }

    @Override
    public boolean removeFavoriteRestaurant(Integer restaurantId, Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new RuntimeException("Guest not found with id: " + guestId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + restaurantId));

        guest.getFavoriteRestaurants().remove(restaurant);  
        guestRepository.saveAndFlush(guest);  
        return true;
    }

    @Override
    public List<Restaurant> getFavoriteRestaurants(Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
            .orElseThrow(() -> new NotFoundException("Guest not found with id: " + guestId));

        return guest.getFavoriteRestaurants().stream()
            .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Restaurant> getRestaurantsByCategories(List<Integer> categoryIds) {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findByRestaurantCategoriesCategoryIdIn(categoryIds);
        return restaurantEntities.stream()
                .map(restaurantEntity -> modelMapper.map(restaurantEntity, Restaurant.class))
                .collect(Collectors.toList());
    }
    
    
}
