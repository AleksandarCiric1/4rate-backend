package com.example.backend4rate.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.services.impl.RestaurantService;
import com.example.backend4rate.exceptions.InvalidPhoneNumberException;
import com.example.backend4rate.exceptions.DuplicatePhoneNumberException;
import com.example.backend4rate.exceptions.RestaurantNotFoundException;
import com.example.backend4rate.exceptions.GuestNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Integer id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurant(id);
            return ResponseEntity.ok(restaurant);
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
        } catch (InvalidPhoneNumberException | DuplicatePhoneNumberException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Integer id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        try {
            boolean updated = restaurantService.updateRestaurantInformation(restaurant);
            if (updated) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (InvalidPhoneNumberException | DuplicatePhoneNumberException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(@RequestParam String name) {
        List<Restaurant> restaurants = restaurantService.searchRestaurant(name);
        return ResponseEntity.ok(restaurants);
    }

    @PostMapping("/{id}/favorite/{guestId}")
    public ResponseEntity<?> addFavoriteRestaurant(@PathVariable Integer id, @PathVariable Integer guestId) {
        try {
            restaurantService.addFavoriteRestaurant(id, guestId);
            return ResponseEntity.ok().build();
        } catch (RestaurantNotFoundException | GuestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}/favorite/{guestId}")
    public ResponseEntity<?> removeFavoriteRestaurant(@PathVariable Integer id, @PathVariable Integer guestId) {
        try {
            restaurantService.removeFavoriteRestaurant(id, guestId);
            return ResponseEntity.ok().build();
        } catch (RestaurantNotFoundException | GuestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/favorites/{guestId}")
    public ResponseEntity<List<Restaurant>> getFavoriteRestaurants(@PathVariable int guestId) {
        try {
            List<Restaurant> favoriteRestaurants = restaurantService.getFavoriteRestaurants(guestId);
            return ResponseEntity.ok(favoriteRestaurants);
        } catch (GuestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCategories(@RequestParam List<Integer> categoryIds) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCategories(categoryIds);
        return ResponseEntity.ok(restaurants);
    }
}
