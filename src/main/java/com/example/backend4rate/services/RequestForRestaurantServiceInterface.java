package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;

import java.util.List;

public interface RequestForRestaurantServiceInterface {
    RequestForRestaurantResponse createRequestForRestaurant(RequestForRestaurant request, Integer managerInteger) throws NotFoundException;

    boolean approveRequest(Integer id);

    boolean denyRequest(Integer id);

    RequestForRestaurantResponse getRequest(Integer id) throws NotFoundException;

    List<RequestForRestaurantResponse> getAllRequest();

    boolean cancelRequestForRestaurant(Integer id);
}
