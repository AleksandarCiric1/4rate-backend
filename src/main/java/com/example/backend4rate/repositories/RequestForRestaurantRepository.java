package com.example.backend4rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.RequestForRestaurantEntity;

@Repository
public interface RequestForRestaurantRepository extends JpaRepository<RequestForRestaurantEntity, Integer> {
    Optional<RequestForRestaurantEntity> findRequestForRestaurantEntityByManagerId(Integer managerId);
}
