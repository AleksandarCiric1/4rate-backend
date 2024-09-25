package com.example.backend4rate.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.dto.RestaurantBlock;
import com.example.backend4rate.models.dto.UpdateRestaurant;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.RestaurantCategoryEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.RestaurantPhoneEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.RestaurantCategoryRepository;
import com.example.backend4rate.repositories.RestaurantPhoneRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final ManagerRepository managerRepository;
    private final RestaurantPhoneRepository restaurantPhoneRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final EmailService emailService;
    private final UserAccountRepository userAccountRepository;
    private final String subject = "4Rate Account";
    private final String body = "Your Account is blocked";
    private final String blockRestaurantMessage = "Your Restaurant has been blocked. Please create new one.";

    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper,
            ManagerRepository managerRepository, RestaurantPhoneRepository restaurantPhoneRepository,
            RestaurantCategoryRepository restaurantCategoryRepository, CategoryRepository categoryRepository,
            EmailService emailService, UserAccountRepository userAccountRepository) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.restaurantPhoneRepository = restaurantPhoneRepository;
        this.restaurantCategoryRepository = restaurantCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.emailService = emailService;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAllByStatus("active");

        if (restaurantEntities.size() > 0) {
            return restaurantEntities.stream().map(entity -> modelMapper.map(entity, Restaurant.class))
                    .collect(Collectors.toList());
        }
        return new ArrayList<Restaurant>();
    }

    @Override
    public boolean blockRestaurant(RestaurantBlock restaurantToBlock) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantToBlock.getId())
                .orElseThrow(
                        () -> new NotFoundException("Couldn't found restaurant!", RestaurantService.class.getName()));

        ManagerEntity managerEntity = managerRepository.findByRestaurantId(restaurantEntity.getId());
        UserAccountEntity userAccountEntity = userAccountRepository.findById(managerEntity.getUserAccount().getId())
                .orElseThrow(() -> new NotFoundException(RestaurantService.class.getName()));
        emailService.sendEmail(userAccountEntity.getEmail(), subject,
                blockRestaurantMessage + "\n Reason: " + restaurantToBlock.getDescription());

        restaurantEntity.setStatus("blocked");
        restaurantRepository.saveAndFlush(restaurantEntity);

        return true;
    }

    @Override
    public Restaurant getRestaurant(Integer userAccountId) throws NotFoundException {
        ManagerEntity managerEntity = managerRepository.findByUserAccountId(userAccountId);

        RestaurantEntity restaurantEntity = managerEntity.getRestaurant();
        return modelMapper.map(restaurantEntity, Restaurant.class);
    }

    @Override
    public Restaurant getRestaurantById(Integer id) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(RestaurantService.class.getName()));

        return modelMapper.map(restaurantEntity, Restaurant.class);
    }

    @Override
    public boolean updateRestaurant(UpdateRestaurant updateRestaurant) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(updateRestaurant.getId())
                .orElseThrow(() -> new NotFoundException(RestaurantService.class.getName()));

        restaurantEntity.setAddress(updateRestaurant.getAddress());
        restaurantEntity.setCity(updateRestaurant.getCity());
        restaurantEntity.setCountry(updateRestaurant.getCountry());
        restaurantEntity.setWorkTime(updateRestaurant.getWorkTime());
        restaurantEntity.setDescription(updateRestaurant.getDescription());
        deletePhonesForRestaurant(restaurantEntity);
        for (String elem : updateRestaurant.getPhones()) {
            RestaurantPhoneEntity restaurantPhoneEntity = new RestaurantPhoneEntity();
            restaurantPhoneEntity.setPhone(elem);
            restaurantPhoneEntity.setRestaurant(restaurantEntity);
            restaurantPhoneRepository.saveAndFlush(restaurantPhoneEntity);
        }
        deleteRestaurantCategoriesForRestaurant(restaurantEntity);
        for (String elem : updateRestaurant.getCategoryIds()) {
            RestaurantCategoryEntity restaurantCategoryEntity = new RestaurantCategoryEntity();
            restaurantCategoryEntity.setRestaurant(restaurantEntity);
            CategoryEntity categoryEntity = categoryRepository.findById(Integer.valueOf(elem))
                    .orElseThrow(() -> new NotFoundException(RestaurantService.class.getName()));
            restaurantCategoryEntity.setCategory(categoryEntity);
            restaurantCategoryRepository.saveAndFlush(restaurantCategoryEntity);
        }

        return true;
    }

    private boolean deletePhonesForRestaurant(RestaurantEntity restaurantEntity) {
        List<RestaurantPhoneEntity> restaurantPhoneEntities = restaurantPhoneRepository
                .findAllByRestaurant(restaurantEntity);

        for (RestaurantPhoneEntity elem : restaurantPhoneEntities) {
            restaurantPhoneRepository.delete(elem);
        }

        return true;
    }

    private boolean deleteRestaurantCategoriesForRestaurant(RestaurantEntity restaurantEntity) {
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryRepository
                .findAllByRestaurant(restaurantEntity);
        for (RestaurantCategoryEntity elem : restaurantCategoryEntities) {
            restaurantCategoryRepository.delete(elem);
        }

        return true;
    }
}
