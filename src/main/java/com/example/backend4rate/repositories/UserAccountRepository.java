package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.dto.User;
import com.example.backend4rate.models.entities.UserAccountEntity;



@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Integer>{
    public UserAccountEntity findByUsername(String username);

    // This query selects all users from user_account and standard_user table
    // Possible change -> to get all users that are not blocked
    @Query("SELECT new com.example.backend4rate.models.dto.User(" +
           "su.id, ua.username, ua.role, ua.status, ua.confirmed as confirmed, ua.email, " +
           "ua.createdAt, ua.avatarUrl, su.dateOfBirth, su.firstName, su.lastName) " +
           "FROM UserAccountEntity ua " +
           "JOIN ua.standardUser su")
    List<User> getAllAccounts();

    @Query("SELECT su.userAccount FROM StandardUserEntity su WHERE su.id = :standardUserId")
    UserAccountEntity getUserAccountByStandardUserId(@Param("standardUserId") Integer standardUserId);
}
