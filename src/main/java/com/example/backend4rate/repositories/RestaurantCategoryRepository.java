package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.RestaurantCategoryEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategoryEntity, Integer> {
    List<RestaurantCategoryEntity> findAllByRestaurant(RestaurantEntity restaurantEntity);
}
