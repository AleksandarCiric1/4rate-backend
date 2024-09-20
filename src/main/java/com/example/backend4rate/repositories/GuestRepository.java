package com.example.backend4rate.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.GuestEntity;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {

    Optional<GuestEntity> findByUserAccount_Id(Integer userAccountId);
}
