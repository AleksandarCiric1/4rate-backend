package com.example.backend4rate.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.models.dto.RestaurantsPerMonth;
import com.example.backend4rate.models.dto.UsersPerMonth;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.AnalyticServiceInterface;

@Service
public class AnalyticService implements AnalyticServiceInterface {

        private final UserAccountRepository userAccountRepository;
        private final RestaurantRepository restaurantRepository;
        private final ModelMapper modelMapper;

        public AnalyticService(UserAccountRepository userAccountRepository, ModelMapper modelMapper,
                        RestaurantRepository restaurantRepository) {
                this.userAccountRepository = userAccountRepository;
                this.modelMapper = modelMapper;
                this.restaurantRepository = restaurantRepository;
        }

        @Override
        public List<UsersPerMonth> getUserCreationStatsForLastYear() {
                LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

                Date oneYearAgoDate = Date.from(oneYearAgo.atZone(ZoneId.systemDefault()).toInstant());

                List<UserAccountEntity> allUsers = userAccountRepository.findAll();

                List<UserAccountEntity> filteredUsers = allUsers.stream()
                                .filter(user -> {
                                        LocalDateTime userCreatedAt = user.getCreatedAt().toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDateTime();
                                        return userCreatedAt.isAfter(oneYearAgo);
                                })
                                .collect(Collectors.toList());

                Map<YearMonth, Long> aggregatedData = filteredUsers.stream()
                                .collect(Collectors.groupingBy(
                                                user -> {
                                                        LocalDateTime dateTime = user.getCreatedAt().toInstant()
                                                                        .atZone(ZoneId.systemDefault())
                                                                        .toLocalDateTime();
                                                        return YearMonth.from(dateTime);
                                                },
                                                Collectors.counting()));

                return aggregatedData.entrySet().stream()
                                .map(entry -> new UsersPerMonth(
                                                entry.getKey().getYear(),
                                                entry.getKey().getMonthValue(),
                                                entry.getValue()))
                                .sorted((a, b) -> {
                                        int yearComparison = Integer.compare(a.getYear(), b.getYear());
                                        return yearComparison != 0 ? yearComparison
                                                        : Integer.compare(a.getMonth(), b.getMonth());
                                })
                                .collect(Collectors.toList());
        }

        @Override
        public List<RestaurantsPerMonth> getRestaurantCreationStatsForLastYear() {
                LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

                Date oneYearAgoDate = Date.from(oneYearAgo.atZone(ZoneId.systemDefault()).toInstant());

                List<RestaurantEntity> allRestaurants = restaurantRepository.findAll();

                List<RestaurantEntity> filteredUsers = allRestaurants.stream()
                                .filter(restaurant -> {
                                        LocalDateTime userCreatedAt = restaurant.getCreatedAt().toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalDateTime();
                                        return userCreatedAt.isAfter(oneYearAgo);
                                })
                                .collect(Collectors.toList());

                Map<YearMonth, Long> aggregatedData = filteredUsers.stream()
                                .collect(Collectors.groupingBy(
                                                user -> {
                                                        LocalDateTime dateTime = user.getCreatedAt().toInstant()
                                                                        .atZone(ZoneId.systemDefault())
                                                                        .toLocalDateTime();
                                                        return YearMonth.from(dateTime);
                                                },
                                                Collectors.counting()));

                return aggregatedData.entrySet().stream()
                                .map(entry -> new RestaurantsPerMonth(
                                                entry.getKey().getYear(),
                                                entry.getKey().getMonthValue(),
                                                entry.getValue()))
                                .sorted((a, b) -> {
                                        int yearComparison = Integer.compare(a.getYear(), b.getYear());
                                        return yearComparison != 0 ? yearComparison
                                                        : Integer.compare(a.getMonth(), b.getMonth());
                                })
                                .collect(Collectors.toList());
        }
}
