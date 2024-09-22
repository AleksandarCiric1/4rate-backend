package com.example.backend4rate.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.models.dto.AnalyticCounts;
import com.example.backend4rate.models.dto.RestaurantsPerMonth;
import com.example.backend4rate.models.dto.UsersPerMonth;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.ReservationRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.ReviewRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.AnalyticServiceInterface;

@Service
public class AnalyticService implements AnalyticServiceInterface {

        private final UserAccountRepository userAccountRepository;
        private final RestaurantRepository restaurantRepository;
        private final ReviewRepository reviewRepository;
        private final ReservationRepository reservationRepository;

        public AnalyticService(UserAccountRepository userAccountRepository, ModelMapper modelMapper,
                        RestaurantRepository restaurantRepository, ReservationRepository reservationRepository,
                        ReviewRepository reviewRepository) {
                this.userAccountRepository = userAccountRepository;
                this.restaurantRepository = restaurantRepository;
                this.reservationRepository = reservationRepository;
                this.reviewRepository = reviewRepository;
        }

        @Override
        public List<UsersPerMonth> getUserCreationStatsForLastYear() {
                LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

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

        public Map<String, Long> getReservationCountsForLastFourMonths(Integer restaurantId) {
                LocalDate currentDate = LocalDate.now();

                Map<String, Long> reservationCounts = new LinkedHashMap<>();

                for (int i = 0; i < 4; i++) {
                        LocalDate monthDate = currentDate.minusMonths(i);
                        int month = monthDate.getMonthValue();
                        int year = monthDate.getYear();

                        Long count = reservationRepository.countReservationsByRestaurantAndMonthAndYear(restaurantId,
                                        month, year);

                        String key = monthDate.getMonth().name().toLowerCase();
                        reservationCounts.put(key, count);
                }

                return reservationCounts;
        }

        @Override
        public AnalyticCounts getAllCounts() {
                AnalyticCounts analyticCounts = new AnalyticCounts();

                analyticCounts.setUsers(userAccountRepository.findAll().size());
                analyticCounts.setRestaurants(restaurantRepository.findAll().size());
                analyticCounts.setReviews(reviewRepository.findAll().size());

                return analyticCounts;
        }
}
