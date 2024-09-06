package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;


import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.StandardUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
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
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    public UserAccountService(ModelMapper modelMapper, UserAccountRepository userAccountRepository, ManagerRepository managerRepository, StandardUserRepository standardUserRepository, AdministratorRepository administratorRepository, GuestRepository guestRepository, PasswordEncoder passwordEncoder){
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.standardUserRepository = standardUserRepository;
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean updateInformation(UpdateInformation updateInformation, Integer id) throws NotFoundException{
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
       
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
    public StandardUser getInformation(Integer id) throws NotFoundException{
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        StandardUser standardUser = new StandardUser();
        standardUser.setUsername(userAccountEntity.getUsername());
        standardUser.setPassword(userAccountEntity.getPassword());
        standardUser.setRole(userAccountEntity.getRole());
        standardUser.setEmail(userAccountEntity.getEmail());

        StandardUserEntity standardUserEntity = standardUserRepository.findByUserAccount(userAccountEntity);
        standardUser.setFirstName(standardUserEntity.getFirstName());
        standardUser.setLastName(standardUserEntity.getLastName());
        standardUser.setDateOfBirth(standardUserEntity.getDateOfBirth());

        if("manager".equals(userAccountEntity.getRole())){
            ManagerEntity managerEntity = managerRepository.findByStandardUser(standardUserRepository);
            standardUser.setContact(managerEntity.getContact());
        }
        else if("guest".equals(userAccountEntity.getRole())){
            GuestEntity guestEntity = guestRepository.findByStandardUser(standardUserEntity);
            standardUser.setContact(guestEntity.getContact());
        }
        else{
            throw new NotFoundException();
        }
        return standardUser;
    }

    @Override
    public UserAccountResponse createAdministratorAccount(UserAccount userAccount) throws NotFoundException, BadRequestException{
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setPassword(passwordEncoder.encode(userAccount.getPassword()));
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
            throw new BadRequestException();
        }
        Optional<UserAccountEntity> optionalManager = userAccountRepository.findById(userAccountEntity.getId());
        return optionalManager.map(manager -> modelMapper.map(manager, UserAccountResponse.class))
                              .orElseThrow(() -> new NotFoundException());
    } 

    @Override
    public List<UserAccountResponse> getAllUserAccount() {
        return userAccountRepository.findAll().stream().map(l -> modelMapper.map(l, UserAccountResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UserAccountResponse getUserAccountById(Integer id) throws NotFoundException {
        return modelMapper.map(userAccountRepository.findById(id).orElseThrow(NotFoundException::new), UserAccountResponse.class);
    }

    @Override
    public void deleteUserAccount(Integer id){
        userAccountRepository.deleteById(id);
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
    public UserAccountResponse createUserAccount(UserAccount userAccount) throws NotFoundException{
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountEntity.setId(null);
        userAccountEntity.setStatus("active");
        userAccountEntity.setCreatedAt(new Date());
        userAccountEntity=userAccountRepository.saveAndFlush(userAccountEntity);

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
    

        Optional<UserAccountEntity> optionalManager = userAccountRepository.findById(userAccountEntity.getId());
        return optionalManager.map(manager -> modelMapper.map(manager, UserAccountResponse.class))
                              .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public UserAccountResponse login(LoginUser loginUser) throws NotFoundException, UnauthorizedException{
        UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(loginUser.getUsername());
        if(userAccountEntity != null && "active".equals(userAccountEntity.getStatus())){
            if(passwordEncoder.matches(loginUser.getPassword(), userAccountEntity.getPassword())){
                return  modelMapper.map(userAccountEntity, UserAccountResponse.class);
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
