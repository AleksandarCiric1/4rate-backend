package com.example.backend4rate.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
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
    // private final ReservationAvailabilityService reservationAvailabilityService;

    public RequestForRestaurantService(RequestForRestaurantRepository requestForRestaurantRepository,
            ModelMapper modelMapper, ManagerRepository managerRepository, RestaurantRepository restaurantRepository) {
        this.requestForRestaurantRepository = requestForRestaurantRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.restaurantRepository = restaurantRepository;
        // this.reservationAvailabilityService=reservationAvailabilityService;
    }

    @Override
    public RequestForRestaurantResponse createRequestForRestaurant(RequestForRestaurant request, Integer userAccountId)
            throws NotFoundException {
        RequestForRestaurantEntity requestForRestaurantEntity = modelMapper.map(request,
                RequestForRestaurantEntity.class);
        requestForRestaurantEntity.setId(null);
        requestForRestaurantEntity.setStatus("pending");

        ManagerEntity managerEntity = managerRepository.findByUserAccountId(userAccountId);
        if (managerEntity == null) {
            throw new NotFoundException(RequestForRestaurantService.class.getName());
        }
        requestForRestaurantEntity.setManager(managerEntity);
        requestForRestaurantEntity = requestForRestaurantRepository.saveAndFlush(requestForRestaurantEntity);
        return modelMapper.map(requestForRestaurantEntity, RequestForRestaurantResponse.class);
    }

    @Override
    public boolean approveRequestForRestaurant(Integer requestId) throws NotFoundException {
        RequestForRestaurantEntity request = requestForRestaurantRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(RequestForRestaurantService.class.getName()));
        ManagerEntity managerEntity = managerRepository.findById(request.getManager().getId())
                .orElseThrow(() -> new NotFoundException(RequestForRestaurantService.class.getName()));

        RestaurantEntity restaurantEntity = new RestaurantEntity();

        restaurantEntity.setName(request.getName());
        restaurantEntity.setWorkTime(request.getWorkTime());
        restaurantEntity.setDescription(request.getDescription());
        restaurantEntity.setCapacity(request.getCapacity());
        restaurantEntity.setCreatedAt(new Date());
        restaurantEntity.setId(null);
        restaurantEntity.setStatus("active");

        restaurantEntity = restaurantRepository.saveAndFlush(restaurantEntity);
        managerEntity.setRestaurant(restaurantEntity);
        managerRepository.save(managerEntity);
        request.setStatus("approved");
        requestForRestaurantRepository.save(request);
        return true;
    }

    @Override
    public boolean denyRequestForRestaurant(Integer requestId) throws NotFoundException {
        return this.changeStatusForRequest(requestId, "denied");
    }

    @Override
    public RequestForRestaurantResponse getRequestForRestaurant(Integer requestId) throws NotFoundException {
        return modelMapper.map(requestForRestaurantRepository.findById(requestId).orElseThrow(() -> new NotFoundException(RequestForRestaurantService.class.getName())),
                RequestForRestaurantResponse.class);
    }

    @Override
    public List<RequestForRestaurantResponse> getAllRequestForRestaurant() {
        List<RequestForRestaurantEntity> requests = requestForRestaurantRepository.findAllByStatus("pending");
        return requests.stream().map(l -> modelMapper.map(l, RequestForRestaurantResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelRequestForRestaurant(Integer requestId) throws NotFoundException {
        return this.changeStatusForRequest(requestId, "canceled");

    }

    private boolean changeStatusForRequest(Integer requestId, String status) throws NotFoundException {
        RequestForRestaurantEntity requestForRestaurantEntity = requestForRestaurantRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(RequestForRestaurantService.class.getName()));
        requestForRestaurantEntity.setStatus(status);
        requestForRestaurantRepository.save(requestForRestaurantEntity);
        return true;
    }

}
