package com.example.backend4rate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.services.impl.ManagerService;

@RestController
@RequestMapping("/v1/manager")
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/restaurant-status/{userAccountId}")
    public ResponseEntity<?> getRestaurantStatus(@PathVariable Integer userAccountId) throws NotFoundException {
        Object response = managerService.checkRestaurantStatus(userAccountId);

        if (response instanceof Restaurant) {
            return ResponseEntity.ok(response);
        }

        if (response instanceof String && response.equals("Request is being processed")) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        if (response instanceof String && "Restaurant is blocked".equals(response)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
