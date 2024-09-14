package com.example.backend4rate.services.impl;

import com.example.backend4rate.exceptions.RestaurantNotFoundException;
import com.example.backend4rate.exceptions.DuplicatePhoneNumberException;
import com.example.backend4rate.exceptions.InvalidPhoneNumberException;
import com.example.backend4rate.exceptions.GuestNotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.*;
import com.example.backend4rate.repositories.CategorySubscriptionRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.RestaurantPhoneRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.services.EmailServiceInterface;
import com.example.backend4rate.services.NotificationServiceInterface;
import com.example.backend4rate.services.RestaurantServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final GuestRepository guestRepository;
    private final RestaurantPhoneRepository restaurantPhoneRepository;
    private final CategorySubscriptionRepository categorySubscriptionRepository;
    private final NotificationServiceInterface notificationService;
    private final EmailServiceInterface emailService;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             GuestRepository guestRepository,
                             CategorySubscriptionRepository categorySubscriptionRepository,
                             NotificationServiceInterface notificationService,
                             EmailServiceInterface emailService,
                             RestaurantPhoneRepository restaurantPhoneRepository,
                             ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.guestRepository = guestRepository;
        this.categorySubscriptionRepository = categorySubscriptionRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.restaurantPhoneRepository = restaurantPhoneRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Restaurant addRestaurant(Restaurant restaurant) throws InvalidPhoneNumberException, DuplicatePhoneNumberException {
        RestaurantEntity restaurantEntity = modelMapper.map(restaurant, RestaurantEntity.class);
        restaurantEntity.setId(null); 
        RestaurantEntity savedEntity = restaurantRepository.saveAndFlush(restaurantEntity);

        validatePhoneNumbers(restaurant.getRestaurantPhones());

        for (String phone : restaurant.getRestaurantPhones()) {
            RestaurantPhoneEntity phoneEntity = new RestaurantPhoneEntity();
            phoneEntity.setPhone(phone);
            phoneEntity.setRestaurant(savedEntity);
            restaurantPhoneRepository.save(phoneEntity);
        }

        notifySubscribers(savedEntity); // Notify subscribers when a new restaurant is added to the category they are subscribed to

        return modelMapper.map(savedEntity, Restaurant.class);
    }

    private void validatePhoneNumbers(List<String> phones) throws InvalidPhoneNumberException, DuplicatePhoneNumberException {
        for (String phone : phones) {
            if (!isValidPhoneNumber(phone)) {
                throw new InvalidPhoneNumberException("Invalid phone number: " + phone);
            }
        }

        Set<String> phoneSet = new HashSet<>();
        for (String phone : phones) {
            if (!phoneSet.add(phone)) {
                throw new DuplicatePhoneNumberException("Duplicate phone number found: " + phone);
            }
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^[+]?\\d[-\\d]*$");
    }
    
    @Override
    public boolean updateRestaurantInformation(Restaurant restaurant) throws InvalidPhoneNumberException, DuplicatePhoneNumberException {
        RestaurantEntity existingRestaurant = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurant.getId()));

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setDescription(restaurant.getDescription());
        existingRestaurant.setWorkTime(restaurant.getWorkTime());

        validatePhoneNumbers(restaurant.getRestaurantPhones());
        updateRestaurantPhones(existingRestaurant, restaurant.getRestaurantPhones());

        restaurantRepository.saveAndFlush(existingRestaurant);

        return true;
    }

    private void updateRestaurantPhones(RestaurantEntity restaurantEntity, List<String> updatedPhones) {
        List<RestaurantPhoneEntity> existingPhones = restaurantPhoneRepository.findByRestaurant(restaurantEntity);

        List<String> updatedPhoneList = new ArrayList<>(updatedPhones);

        List<RestaurantPhoneEntity> phonesToDelete = existingPhones.stream()
                .filter(phone -> !updatedPhoneList.contains(phone.getPhone()))
                .collect(Collectors.toList());

        for (RestaurantPhoneEntity phoneToDelete : phonesToDelete) {
            restaurantPhoneRepository.delete(phoneToDelete);
        }

        List<String> existingPhoneList = existingPhones.stream()
                .map(RestaurantPhoneEntity::getPhone)
                .collect(Collectors.toList());

        List<String> phonesToAdd = updatedPhones.stream()
                .filter(phone -> !existingPhoneList.contains(phone))
                .collect(Collectors.toList());

        for (String phone : phonesToAdd) {
            RestaurantPhoneEntity phoneEntity = new RestaurantPhoneEntity();
            phoneEntity.setPhone(phone);
            phoneEntity.setRestaurant(restaurantEntity);
            restaurantPhoneRepository.save(phoneEntity);
        }
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
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));
        return modelMapper.map(restaurant, Restaurant.class);
    }

    @Override
    public List<Restaurant> searchRestaurant(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
                .map(restaurant -> modelMapper.map(restaurant, Restaurant.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addFavoriteRestaurant(Integer restaurantId, Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));

        guest.getFavoriteRestaurants().add(restaurant);
        guestRepository.saveAndFlush(guest);
        return true;
    }

    @Override
    public boolean removeFavoriteRestaurant(Integer restaurantId, Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));

        guest.getFavoriteRestaurants().remove(restaurant);
        guestRepository.saveAndFlush(guest);
        return true;
    }

    @Override
    public List<Restaurant> getFavoriteRestaurants(Integer guestId) {
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found with id: " + guestId));

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
        // Get the categories of the restaurant
        List<CategoryEntity> categories = restaurantEntity.getRestaurantCategories().stream()
                .map(RestaurantCategoryEntity::getCategory)
                .collect(Collectors.toList());

        for (CategoryEntity category : categories) {
            // Get all subscriptions for the categories
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

                            NotificationEntity notification = new NotificationEntity();
                            notification.setContent(body);
                            notification.setDate(new Date());
                            notification.setStatus(false); // Unread
                            notification.setStandardUser(guest.getStandardUser());
                            notificationService.saveNotification(notification);
                        });
            }
        }
    }
}
