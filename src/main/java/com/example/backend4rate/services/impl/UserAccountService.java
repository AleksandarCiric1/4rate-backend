package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.entities.AdministratorEntity;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.StandardUserEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.repositories.AdministratorRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.repositories.StandardUserRepository;
import com.example.backend4rate.services.UserAccountServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;



@Service
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final ManagerRepository managerRepository;
    private final StandardUserRepository standardUserRepository;
    private final AdministratorRepository administratorRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public UserAccountService(ModelMapper modelMapper, UserAccountRepository userAccountRepository, ManagerRepository managerRepository, StandardUserRepository standardUserRepository, AdministratorRepository administratorRepository, GuestRepository guestRepository){
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.standardUserRepository = standardUserRepository;
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public boolean updateInformation(UpdateInformation updateInformation) throws NotFoundException{
        UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(updateInformation.getUsername());
        if(userAccountEntity == null){
            throw new NotFoundException();
        }
        userAccountEntity.setUsername(updateInformation.getUsername());
        userAccountEntity.setAvatarUrl(updateInformation.getAvatarUrl());
        userAccountEntity.setEmail(updateInformation.getEmail());
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);

        if(!"administrator".equals(userAccountEntity.getRole())){
            StandardUserEntity standardUserEntity = standardUserRepository.findByUserAccount(userAccountEntity);
            standardUserEntity.setFirstName(updateInformation.getFirstName());
            standardUserEntity.setLastName(updateInformation.getLastName());
            standardUserEntity.setDateOfBirth(updateInformation.getDateOfBirth());
            standardUserEntity = standardUserRepository.saveAndFlush(standardUserEntity);
                if("manager".equals(userAccountEntity.getRole())){
                    ManagerEntity managerEntity = managerRepository.findByStandardUser(standardUserRepository);
                    managerEntity.setContact(updateInformation.getContact());
                    managerEntity = managerRepository.saveAndFlush(managerEntity);
                }
                else{
                    GuestEntity guestEntity = guestRepository.findByStandardUser(standardUserEntity);
                    guestEntity.setContact(updateInformation.getContact());
                    guestEntity = guestRepository.saveAndFlush(guestEntity);
                }
        }

        return true;
    }

    @Override
    public UserAccount getInformation(Integer id) throws NotFoundException{
       return this.getUserAccountById(id);
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
    public boolean suspendUserAccount(Integer id) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        userAccountEntity.setStatus("suspended");
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        if(userAccountEntity.getStatus().equals("suspended"))
            return true;
        else
            return false;
    }

    public boolean unsuspendUserAccount(Integer id) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        userAccountEntity.setStatus("active");
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        if(userAccountEntity.getStatus().equals("active"))
            return true;
        else
            return false;
    }

    
    @Override
    public UserAccount createUserAccount(UserAccount userAccount) throws NotFoundException{
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setId(null);
        userAccountEntity.setStatus("active");
        userAccountEntity.setCreatedAt(new Date());
        userAccountEntity=userAccountRepository.saveAndFlush(userAccountEntity);

        if(userAccount.getRole().equals("administrator")){
            AdministratorEntity administratorEntity = new AdministratorEntity();
            administratorEntity.setUserAccount(userAccountEntity);
            administratorEntity.setId(null);
            administratorEntity=administratorRepository.saveAndFlush(administratorEntity);
        }
        else{
            StandardUserEntity standardUserEntity = new StandardUserEntity();
            standardUserEntity.setUserAccount(userAccountEntity);
            standardUserEntity.setId(null);
            standardUserEntity=standardUserRepository.saveAndFlush(standardUserEntity);
            if(userAccount.getRole().equals("manager")){
                ManagerEntity managerEntity = new ManagerEntity();
                managerEntity.setStandardUser(standardUserEntity);
                managerEntity.setId(null);
                managerEntity=managerRepository.saveAndFlush(managerEntity);
            } 
            else{
                GuestEntity guestEntity = new GuestEntity();
                guestEntity.setStandardUser(standardUserEntity);
                guestEntity.setId(null);
                guestEntity=guestRepository.saveAndFlush(guestEntity);
            }
        }

        Optional<UserAccountEntity> optionalManager = userAccountRepository.findById(userAccountEntity.getId());
        return optionalManager.map(manager -> modelMapper.map(manager, UserAccount.class))
                              .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public UserAccount login(LoginUser loginUser) throws NotFoundException, UnauthorizedException{
        UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(loginUser.getUsername());
        if(userAccountEntity != null){
            if(userAccountEntity.getPassword().equals(loginUser.getPassword())){
                return  modelMapper.map(userAccountEntity, UserAccount.class);
            }
            else{
                throw new UnauthorizedException(); 
            }
        }
        else{
            throw new NotFoundException();
        }
    }
    
}
