package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.AuthServiceInterface;

@Service
public class AuthService implements AuthServiceInterface{

    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = new ModelMapper();
    }

    public List<UserAccount> getAllUserAccounts(){
        List<UserAccountEntity> accounts = this.userAccountRepository.findAll();
        

        return accounts.stream().map(elem -> modelMapper.map(elem, UserAccount.class))
            .collect(Collectors.toList());
    }
}
