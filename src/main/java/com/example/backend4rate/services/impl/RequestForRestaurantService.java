package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.ProcessRequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.RequestForRestaurantEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.RequestForRestaurantRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.RequestForRestaurantServiceInterface;

@Service
public class RequestForRestaurantService implements RequestForRestaurantServiceInterface {
    private final RequestForRestaurantRepository requestForRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;
    //private final ReservationAvailabilityService reservationAvailabilityService;

    public RequestForRestaurantService(RequestForRestaurantRepository requestForRestaurantRepository,
            ModelMapper modelMapper, ManagerRepository managerRepository, RestaurantRepository restaurantRepository) {
        this.requestForRestaurantRepository = requestForRestaurantRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.restaurantRepository = restaurantRepository;
        //this.reservationAvailabilityService=reservationAvailabilityService;
    }

    @Override
    public RequestForRestaurantResponse createRequestForRestaurant(RequestForRestaurant request, Integer userAccountId)
            throws NotFoundException {
        RequestForRestaurantEntity requestForRestaurantEntity = modelMapper.map(request,
                RequestForRestaurantEntity.class);
        requestForRestaurantEntity.setId(null);

        ManagerEntity managerEntity = managerRepository.findByUserAccountId(userAccountId);
        if (managerEntity == null) {
            throw new NotFoundException();
        }
        requestForRestaurantEntity.setManager(managerEntity);
        requestForRestaurantEntity = requestForRestaurantRepository.saveAndFlush(requestForRestaurantEntity);
        return modelMapper.map(requestForRestaurantEntity, RequestForRestaurantResponse.class);
    }

    @Override
    public boolean approveRequestForRestaurant(Integer requestId) throws NotFoundException {
        RequestForRestaurantResponse request = this.getRequestForRestaurant(requestId);
        ManagerEntity managerEntity = managerRepository.findById(request.getManagerId())
                .orElseThrow(NotFoundException::new);

        RestaurantEntity restaurantEntity = new RestaurantEntity();

        restaurantEntity.setName(request.getName());
        restaurantEntity.setWorkTime(request.getWorkTime());
        restaurantEntity.setDescription(request.getDescription());
        restaurantEntity.setCapacity(request.getCapacity());
        restaurantEntity.setId(null);
        restaurantEntity.setStatus("active");

        if (!this.deleteRequest(requestId)) {
            return false;
        }

        restaurantEntity = restaurantRepository.saveAndFlush(restaurantEntity);
        managerEntity.setRestaurant(restaurantEntity);
        //reservationAvailabilityService.createReservationAvailability(restaurantEntity);
        managerRepository.saveAndFlush(managerEntity);

        return true;
    }

    @Override
    public boolean denyRequestForRestaurant(Integer requestId) throws NotFoundException {
        return this.deleteRequest(requestId);
    }

    @Override
    public RequestForRestaurantResponse getRequestForRestaurant(Integer id) throws NotFoundException {
        return modelMapper.map(requestForRestaurantRepository.findById(id).orElseThrow(NotFoundException::new),
                RequestForRestaurantResponse.class);
    }

    @Override
    public List<RequestForRestaurantResponse> getAllRequestForRestaurant() {
        return requestForRestaurantRepository.findAll().stream()
                .map(l -> modelMapper.map(l, RequestForRestaurantResponse.class)).collect(Collectors.toList());
    }

    @Override
    public boolean cancelRequestForRestaurant(Integer id) {
        return this.deleteRequest(id);
    }

    private boolean deleteRequest(Integer id) {
        Optional<RequestForRestaurantEntity> requestOptional = requestForRestaurantRepository.findById(id);
        if (requestOptional.isPresent()) {
            requestForRestaurantRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
