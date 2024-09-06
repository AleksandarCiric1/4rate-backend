package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.RequestForRestaurant;
import com.example.backend4rate.models.dto.RequestForRestaurantResponse;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.RequestForRestaurantEntity;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.RequestForRestaurantRepository;
import com.example.backend4rate.services.RequestForRestaurantServiceInterface;

@Service
public class RequestForRestaurantService implements RequestForRestaurantServiceInterface{
    private final RequestForRestaurantRepository requestForRestaurantRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;

    public RequestForRestaurantService(RequestForRestaurantRepository requestForRestaurantRepository, ModelMapper modelMapper, ManagerRepository managerRepository){
        this.requestForRestaurantRepository = requestForRestaurantRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
    }

    @Override
    public RequestForRestaurantResponse createRequestForRestaurant(RequestForRestaurant request, Integer managerId) throws NotFoundException{
        RequestForRestaurantEntity requestForRestaurantEntity = modelMapper.map(request, RequestForRestaurantEntity.class);
        requestForRestaurantEntity.setId(null);

        ManagerEntity managerEntity = managerRepository.findById(managerId).orElseThrow(NotFoundException::new);
        requestForRestaurantEntity.setManager(managerEntity);
        requestForRestaurantEntity = requestForRestaurantRepository.saveAndFlush(requestForRestaurantEntity);

        return modelMapper.map(requestForRestaurantEntity, RequestForRestaurantResponse.class);     
    }

    @Override
    public boolean approveRequest(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveRequest'");
    }

    @Override
    public boolean denyRequest(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'denyRequest'");
    }

    @Override
    public RequestForRestaurantResponse getRequest(Integer id) throws NotFoundException{
        return modelMapper.map(requestForRestaurantRepository.findById(id).orElseThrow(NotFoundException::new), RequestForRestaurantResponse.class);
    }

    @Override
    public List<RequestForRestaurantResponse> getAllRequest() {
        return requestForRestaurantRepository.findAll().stream().map(l -> modelMapper.map(l, RequestForRestaurantResponse.class)).collect(Collectors.toList());
    }

    @Override
    public boolean cancelRequestForRestaurant(Integer id) {
        return this.deleteRequest(id);
    }

    private boolean deleteRequest(Integer id){
        Optional<RequestForRestaurantEntity> requestOptional =  requestForRestaurantRepository.findById(id);
        if(requestOptional.isPresent()){
            requestForRestaurantRepository.deleteById(id);
            return true;
        }
        else{
            return false;
        }
    }
    
}
