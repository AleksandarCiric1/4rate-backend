package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;
import com.example.backend4rate.services.impl.RequestForRestaurantService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/v1/requestForRestaurants")
public class RequestForRestaurantController {
    private final RequestForRestaurantService requestForRestaurantService;

    public RequestForRestaurantController(RequestForRestaurantService requestForRestaurantService){
        this.requestForRestaurantService = requestForRestaurantService;
    }
    
    @PostMapping("/createRequest/{managerId}")
     public RequestForRestaurantResponse createRequestForRestaurant(@RequestBody RequestForRestaurant requestForRestaurant,@PathVariable Integer managerId) throws NotFoundException{
        return requestForRestaurantService.createRequestForRestaurant(requestForRestaurant, managerId);
    }

    @GetMapping("/getRequest/{id}")
    public RequestForRestaurantResponse getRequestForRestaurant(@PathVariable Integer id) throws NotFoundException{
        return requestForRestaurantService.getRequest(id);
    }
}
