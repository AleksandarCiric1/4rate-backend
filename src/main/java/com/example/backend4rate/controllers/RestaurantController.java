package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.dto.RestaurantBlock;
import com.example.backend4rate.services.RestaurantServiceInterface;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {

    private final RestaurantServiceInterface restaurantService;

    public RestaurantController(RestaurantServiceInterface restaurantService){
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<Restaurant> restaurants =  restaurantService.getAll();
        if (restaurants.size() > 0){
            return ResponseEntity.ok().body(restaurants);
        }else{
            return ResponseEntity.badRequest().body("There is no restaurants");
        }
    }
    
    @PutMapping("/block")
    public ResponseEntity<?> blockRestaurant(@RequestBody RestaurantBlock restaurantToBlock) throws NotFoundException {
        if (restaurantService.blockRestaurant(restaurantToBlock)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
