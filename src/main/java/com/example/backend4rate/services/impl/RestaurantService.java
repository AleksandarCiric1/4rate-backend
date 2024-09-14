package com.example.backend4rate.services.impl;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.*;
import com.example.backend4rate.repositories.CategorySubscriptionRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.services.EmailServiceInterface;
import com.example.backend4rate.services.NotificationServiceInterface;
import com.example.backend4rate.services.RestaurantServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final GuestRepository guestRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final NotificationServiceInterface notificationService;
    private final EmailServiceInterface emailService;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             GuestRepository guestRepository,
                             CategorySubscriptionRepository categorySubscriptionRepository,
                             NotificationServiceInterface notificationService,
                             EmailServiceInterface emailService,
                             ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.guestRepository = guestRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantEntity.setId(null); 
        RestaurantEntity savedEntity = restaurantRepository.saveAndFlush(restaurantEntity);

        notifySubscribers(savedEntity); // Notify subscribers about the new restaurant

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

    private void notifySubscribers(RestaurantEntity restaurantEntity) {
        List<CategoryEntity> categories = restaurantEntity.getRestaurantCategories().stream()
                .map(RestaurantCategoryEntity::getCategory)
                .collect(Collectors.toList());

        for (CategoryEntity category : categories) {
            List<CategorySubscriptionEntity> subscriptions = categorySubscriptionRepository.findByCategory(category);

            for (CategorySubscriptionEntity subscription : subscriptions) {
                GuestEntity guest = subscription.getGuest();
                Optional.ofNullable(guest)
                        .map(GuestEntity::getStandardUser)
                        .map(StandardUserEntity::getUserAccount)
                        .map(UserAccountEntity::getEmail)
                        .ifPresent(email -> {
                            
                            String subject = "New Restaurant Added!";
                            String body = "A new restaurant, " + restaurantEntity.getName() + ", has been added to the category: " + category.getName();

                            emailService.sendEmail(email, subject, body);

                           
                        });
            }
        }
    }
}
