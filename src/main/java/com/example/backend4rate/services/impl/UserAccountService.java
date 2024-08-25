package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.LoginUser;

import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.UserAccountServiceInterface;

@Service
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    public UserAccountService(ModelMapper modelMapper, UserAccountRepository userAccountRepository){
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean updateInformation(UserAccount userAccount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInformation'");
    }

    @Override
    public UserAccount getInformation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInformation'");
    }

    @Override
    public List<UserAccount> getAllUserAccount() {
        return userAccountRepository.findAll().stream().map(l -> modelMapper.map(l, UserAccount.class)).collect(Collectors.toList());
    }

    @Override
    public UserAccount getUserAccountById(Integer id) throws NotFoundException {
        return modelMapper.map(userAccountRepository.findById(id).orElseThrow(NotFoundException::new), UserAccount.class);
    }

    @Override
    public boolean deleteUserAccount(Integer id) {
        userAccountRepository.deleteById(id);
        Optional<UserAccountEntity> deletedUserAccount = userAccountRepository.findById(id);
        return deletedUserAccount.isEmpty();
    }

    @Override
    public boolean suspendUserAccount(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'suspendUserAccount'");
    }

    @Override
    public UserAccount createManagerAccount(UserAccount userAccount) throws NotFoundException{
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setId(null);
        userAccountEntity=userAccountRepository.saveAndFlush(userAccountEntity);
        return getUserAccountById(userAccountEntity.getId());
    }

    @Override
    public boolean login(LoginUser loginUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }
    
}
