package com.example.backend4rate.services;

import com.example.backend4rate.exceptions.NotFoundException;

public interface ManagerServiceInterface {
    public Object checkRestaurantStatus(Integer userAccountId) throws NotFoundException;
}
