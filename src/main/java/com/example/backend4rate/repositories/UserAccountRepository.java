package com.example.backend4rate.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend4rate.models.entities.UserAccountEntity;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Integer> {
        public UserAccountEntity findByUsername(String username);

        @Query("SELECT COUNT(u) FROM UserAccountEntity u WHERE " +
        "MONTH(u.createdAt) = :month AND YEAR(u.createdAt) = :year")
        Long countUserAccountsByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

        // @Query("SELECT new com.example.backend4rate.models.dto.User(" +
        // "su.id, ua.id as userAccountId, ua.username, ua.role, ua.status, ua.confirmed
        // as confirmed, ua.email, "
        // +
        // "ua.createdAt, ua.avatarUrl, su.dateOfBirth, su.firstName, su.lastName) " +
        // "FROM UserAccountEntity ua " +
        // "JOIN ua.standardUser su")
        // List<User> getAllUsers();

        // This query selects all users from user_account and standard_user table
        // Possible change -> to get all users that are not blocked
        // @Query("SELECT new com.example.backend4rate.models.dto.User(" +
        // "su.id, ua.id as userAccountId, ua.username, ua.role, ua.status, ua.confirmed
        // as confirmed, ua.email, "
        // +
        // "ua.createdAt, ua.avatarUrl, su.dateOfBirth, su.firstName, su.lastName) " +
        // "FROM UserAccountEntity ua " +
        // "JOIN ua.standardUser su")
        // List<User> getAllStandardUserAccounts();

        // @Query("SELECT new com.example.backend4rate.models.dto.User(" +
        // "a.id, ua.id as userAccountId, ua.username, ua.role, ua.status, ua.confirmed
        // as confirmed, ua.email, "
        // +
        // "ua.createdAt, ua.avatarUrl, a.dateOfBirth, a.firstName, a.lastName) " +
        // "FROM UserAccountEntity ua " +
        // "JOIN ua.administrator a")
        // List<User> getAllAdministratorAccounts();

        // @Query("SELECT new com.example.backend4rate.models.dto.User(" +
        // "a.id, ua.id as userAccountId, ua.username, ua.role, ua.status, ua.confirmed
        // as confirmed, ua.email, "
        // +
        // "ua.createdAt, ua.avatarUrl, a.dateOfBirth, a.firstName, a.lastName) " +
        // "FROM UserAccountEntity ua " +
        // "JOIN ua.administrator a WHERE ua.id = :userAccountId")
        // User getAdminUserByUserAccountId(@Param("userAccountId") Integer
        // userAccountId);

        // @Query("SELECT new com.example.backend4rate.models.dto.User(" +
        // "su.id, ua.id as userAccountId, ua.username, ua.role, ua.status, ua.confirmed
        // as confirmed, ua.email, "
        // +
        // "ua.createdAt, ua.avatarUrl, su.dateOfBirth, su.firstName, su.lastName) " +
        // "FROM UserAccountEntity ua " +
        // "JOIN ua.standardUser su WHERE ua.id = :userAccountId")
        // User getStandardUserByUserAccountId(@Param("userAccountId") Integer
        // userAccountId);

        // @Query("SELECT su.userAccount FROM StandardUserEntity su WHERE su.id =
        // :standardUserId")
        // UserAccountEntity getUserAccountByStandardUserId(@Param("standardUserId")
        // Integer standardUserId);

        // @Query("SELECT a.userAccount FROM AdministratorEntity a WHERE a.id =
        // :administratorId")
        // UserAccountEntity getUserAccountByAdministratorId(@Param("administratorId")
        // Integer administratorId);
}
