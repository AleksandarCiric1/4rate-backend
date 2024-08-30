package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.StandardUser;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.StandardUserEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.StandardUserRepository;
import com.example.backend4rate.services.UserAccountServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final ManagerRepository managerRepository;
    private final StandardUserRepository standardUserRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public UserAccountService(ModelMapper modelMapper, UserAccountRepository userAccountRepository, ManagerRepository managerRepository, StandardUserRepository standardUserRepository){
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.standardUserRepository = standardUserRepository;
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
    public StandardUser createManagerAccount(StandardUser standardUser) throws NotFoundException{
        UserAccountEntity userAccountEntity = modelMapper.map(standardUser, UserAccountEntity.class);
        userAccountEntity.setId(null);
        userAccountEntity=userAccountRepository.saveAndFlush(userAccountEntity);
        entityManager.refresh(userAccountEntity);

        StandardUserEntity standardUserEntity = modelMapper.map(standardUser, StandardUserEntity.class);
        standardUserEntity.setId(null);
        standardUserEntity.setUserAccount(userAccountEntity);
        standardUserEntity=standardUserRepository.saveAndFlush(standardUserEntity);
        entityManager.refresh(standardUserEntity);

        ManagerEntity managerEntity = modelMapper.map(standardUser, ManagerEntity.class);
        managerEntity.setId(null);
        managerEntity.setStandardUser(standardUserEntity);
        managerEntity=managerRepository.saveAndFlush(managerEntity);
        entityManager.refresh(managerEntity);
        Optional<ManagerEntity> optionalManager = managerRepository.findById(managerEntity.getId());
        return optionalManager.map(manager -> modelMapper.map(manager, StandardUser.class))
                              .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public boolean login(LoginUser loginUser) {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }
    
}
