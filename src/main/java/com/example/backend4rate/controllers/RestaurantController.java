package com.example.backend4rate.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.services.impl.RestaurantService;


@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Integer id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurant(id);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.addRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.updateRestaurantInformation(restaurant);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<Restaurant> searchRestaurants(@RequestParam String name) {
        return restaurantService.searchRestaurant(name);
    }

    @PostMapping("/{id}/favorite/{guestId}")
    public ResponseEntity<?> addFavoriteRestaurant(@PathVariable Integer id, @PathVariable Integer guestId) {
        restaurantService.addFavoriteRestaurant(id, guestId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite/{guestId}")
    public ResponseEntity<?> removeFavoriteRestaurant(@PathVariable Integer id, @PathVariable Integer guestId) {
        restaurantService.removeFavoriteRestaurant(id, guestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites/{guestId}")
    public List<Restaurant> getFavoriteRestaurants(@PathVariable int guestId) {
        return restaurantService.getFavoriteRestaurants(guestId);
    }
    
    @GetMapping("/categories")
    public List<Restaurant> getRestaurantsByCategories(@RequestParam List<Integer> categoryIds) {
        return restaurantService.getRestaurantsByCategories(categoryIds);
    }
    
}
