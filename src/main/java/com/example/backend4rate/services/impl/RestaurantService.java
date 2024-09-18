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
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.RestaurantCategoryRepository;
import com.example.backend4rate.repositories.RestaurantPhoneRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final ManagerRepository managerRepository;
    private final RestaurantPhoneRepository restaurantPhoneRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CategoryRepository categoryRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper,
            ManagerRepository managerRepository, RestaurantPhoneRepository restaurantPhoneRepository,
            RestaurantCategoryRepository restaurantCategoryRepository, CategoryRepository categoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.restaurantPhoneRepository = restaurantPhoneRepository;
        this.restaurantCategoryRepository = restaurantCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

        if (restaurantEntities.size() > 0) {
            return restaurantEntities.stream().map(entity -> modelMapper.map(entity, Restaurant.class))
                    .collect(Collectors.toList());
        }
        return new ArrayList<Restaurant>();
    }

    @Override
    public boolean blockRestaurant(RestaurantBlock restaurantToBlock) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantToBlock.getId())
                .orElseThrow(() -> new NotFoundException("Couldn't found restaurant!"));

        restaurantEntity.setStatus("blocked");
        restaurantRepository.saveAndFlush(restaurantEntity);
        // TO DO , ako je blokiranje proslo uspjesno neophodno je iskoristiti mail
        // servis za slanje mejla na
        // osnovu description u restaurantToBlock objekta
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
                .orElseThrow(NotFoundException::new);

        return modelMapper.map(restaurantEntity, Restaurant.class);
    }

    @Override
    public boolean updateRestaurant(UpdateRestaurant updateRestaurant) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(updateRestaurant.getId())
                .orElseThrow(NotFoundException::new);

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
                    .orElseThrow(NotFoundException::new);
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
