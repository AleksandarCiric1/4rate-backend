package com.example.backend4rate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.GuestEntity;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {
    GuestEntity findByUserAccountId(Integer userAccountId);
}
