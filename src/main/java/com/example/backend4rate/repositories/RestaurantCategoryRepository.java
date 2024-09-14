package com.example.backend4rate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend4rate.models.entities.RestaurantCategoryEntity;
import com.example.backend4rate.models.entities.CategoryEntity;

import java.util.List;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategoryEntity, Integer> {

    List<RestaurantCategoryEntity> findByCategoryIn(List<CategoryEntity> categories);
    
}
