package com.example.backend4rate.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.dto.RestaurantBlock;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;

@Service
public class RestaurantService implements RestaurantServiceInterface{

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper){
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Restaurant> getAll(){
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

        if (restaurantEntities.size() > 0){
            return restaurantEntities.stream().map(entity ->  modelMapper.map(entity, Restaurant.class))
            .collect(Collectors.toList());
        }
        return new ArrayList<Restaurant>();
    }

    @Override
    public boolean blockRestaurant(RestaurantBlock restaurantToBlock) throws NotFoundException{
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantToBlock.getId())
        .orElseThrow(() -> new NotFoundException("Couldn't found restaurant!"));

        restaurantEntity.setStatus("blocked");
        if (restaurantRepository.saveAndFlush(restaurantEntity) != null ) 
        {
            // TO DO , ako je blokiranje proslo uspjesno neophodno je iskoristiti mail servis za slanje mejla na 
            // osnovu description u restaurantToBlock objekta
            return true;
        }
        return false;
    }
}
