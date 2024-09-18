package com.example.backend4rate.services.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.RequestForRestaurantEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.RequestForRestaurantRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.ManagerServiceInterface;

@Service
public class ManagerService implements ManagerServiceInterface {

    private final ManagerRepository managerRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final RequestForRestaurantRepository requestForRestaurantRepository;

    public ManagerService(ManagerRepository managerRepository, RestaurantRepository restaurantRepository,
            RequestForRestaurantRepository requestForRestaurantRepository, ModelMapper modelMapper) {
        this.managerRepository = managerRepository;
        this.restaurantRepository = restaurantRepository;
        this.requestForRestaurantRepository = requestForRestaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Object checkRestaurantStatus(Integer userAccountId) throws NotFoundException {
        // Get Manager
        ManagerEntity managerEntity = managerRepository.findByUserAccountId(userAccountId);
        Integer managerId = managerEntity.getId();

        RestaurantEntity restaurantEntity = managerEntity.getRestaurant();
        if (restaurantEntity != null) {
            // Check if manager already has a restaurant
            if ("active".equals(restaurantEntity.getStatus()))
                return modelMapper.map(restaurantEntity, Restaurant.class);

            // Check if restaurant is blocked
            else if ("blocked".equals(restaurantEntity.getStatus()))
                return "Restaurant is blocked";
        }

        // Check if there is pending request
        Optional<RequestForRestaurantEntity> pendingRequest = requestForRestaurantRepository
                .findRequestForRestaurantEntityByManagerId(managerId);
        if (pendingRequest.isPresent()) {
            return "Request is being processed";
        }

        return "Create new request";
    }

}
