package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.ProcessRequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;
import com.example.backend4rate.services.impl.RequestForRestaurantService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/v1/requestForRestaurants")
public class RequestForRestaurantController {
    private final RequestForRestaurantService requestForRestaurantService;

    public RequestForRestaurantController(RequestForRestaurantService requestForRestaurantService) {
        this.requestForRestaurantService = requestForRestaurantService;
    }

    @PostMapping("/createRequest/{userAccountId}")
    public RequestForRestaurantResponse createRequestForRestaurant(
            @RequestBody RequestForRestaurant requestForRestaurant, @PathVariable Integer userAccountId)
            throws NotFoundException {
        return requestForRestaurantService.createRequestForRestaurant(requestForRestaurant, userAccountId);
    }

    @GetMapping("/getRequest/{id}")
    public RequestForRestaurantResponse getRequestForRestaurant(@PathVariable Integer id) throws NotFoundException {
        return requestForRestaurantService.getRequestForRestaurant(id);
    }

    @GetMapping("/getAllRequests")
    public List<RequestForRestaurantResponse> getAllRequestForRestaurant() {
        return requestForRestaurantService.getAllRequestForRestaurant();
    }

    @DeleteMapping("/cancelRequest/{id}")
    public boolean cancelRequestForRestaurant(@PathVariable Integer id) throws NotFoundException{
        return requestForRestaurantService.cancelRequestForRestaurant(id);
    }

    @PutMapping("/denyRequest/{requestId}")
    public ResponseEntity<?> denyRequestForRestaurant(@PathVariable Integer requestId)
            throws NotFoundException {
        if (requestForRestaurantService.denyRequestForRestaurant(requestId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/approveRequest/{requestId}")
    public ResponseEntity<?> approveRequestForRestaurant(@PathVariable Integer requestId)
            throws NotFoundException {
        if (requestForRestaurantService.approveRequestForRestaurant(requestId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
