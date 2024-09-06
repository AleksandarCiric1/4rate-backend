package com.example.backend4rate.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.CommentEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    Collection<CommentEntity> findByRestaurant(RestaurantEntity restaurantEntity);
    
}
