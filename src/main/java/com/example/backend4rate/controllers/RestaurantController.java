package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.dto.RestaurantBlock;
import com.example.backend4rate.models.dto.UpdateRestaurant;
import com.example.backend4rate.services.RestaurantServiceInterface;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {

    private final RestaurantServiceInterface restaurantService;

    public RestaurantController(RestaurantServiceInterface restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(restaurantService.getAll());
    }

    @GetMapping("/getRestaurant/{userAccountId}")
    public ResponseEntity<?> getRestaurant(@PathVariable Integer userAccountId) throws NotFoundException {
        Restaurant restaurant = restaurantService.getRestaurant(userAccountId);
        if (restaurant != null) {
            return ResponseEntity.ok().body(restaurant);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getRestaurantById/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Integer id) throws NotFoundException {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
            return ResponseEntity.ok().body(restaurant);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateRestaurant")
    public ResponseEntity<?> updateRestaurant(@RequestBody UpdateRestaurant updateRestaurant) throws NotFoundException {
        if (restaurantService.updateRestaurant(updateRestaurant)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/block")
    public ResponseEntity<?> blockRestaurant(@RequestBody RestaurantBlock restaurantToBlock) throws NotFoundException {
        if (restaurantService.blockRestaurant(restaurantToBlock)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
