package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;

import java.util.List;

public interface RequestForRestaurantServiceInterface {
    RequestForRestaurantResponse createRequestForRestaurant(RequestForRestaurant request, Integer managerInteger) throws NotFoundException;

    void approveRequestForRestaurant(Integer id) throws NotFoundException;

    boolean denyRequestForRestaurant(Integer id);

    RequestForRestaurantResponse getRequestForRestaurant(Integer id) throws NotFoundException;

    List<RequestForRestaurantResponse> getAllRequestForRestaurant();

    boolean cancelRequestForRestaurant(Integer id);
}
