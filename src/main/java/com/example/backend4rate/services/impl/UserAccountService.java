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
import com.example.backend4rate.models.dto.PasswordChange;
import com.example.backend4rate.models.dto.User;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.models.dto.UserUpdateDTO;
import com.example.backend4rate.models.entities.AdministratorEntity;
import com.example.backend4rate.models.entities.GuestEntity;
import com.example.backend4rate.models.entities.ManagerEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.repositories.AdministratorRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.ManagerRepository;
import com.example.backend4rate.services.UserAccountServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserAccountService implements UserAccountServiceInterface {
    private final UserAccountRepository userAccountRepository;
    private final ManagerRepository managerRepository;
    private final AdministratorRepository administratorRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String subject = "4Rate Account";
    private final String body = "Your Account is blocked";
    @PersistenceContext
    private EntityManager entityManager;

    public UserAccountService(ModelMapper modelMapper, UserAccountRepository userAccountRepository,
            ManagerRepository managerRepository,
            AdministratorRepository administratorRepository, GuestRepository guestRepository,
            PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userAccountRepository = userAccountRepository;
        this.modelMapper = modelMapper;
        this.managerRepository = managerRepository;
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public User createAdministratorAccount(UserAccount userAccount)
            throws NotFoundException, BadRequestException {
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountEntity.setId(null);
        userAccountEntity.setStatus("active");
        userAccountEntity.setCreatedAt(new Date());
        userAccountEntity.setConfirmed(true);
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        entityManager.refresh(userAccountEntity);

        if (userAccount.getRole().equals("administrator")) {
            AdministratorEntity administratorEntity = new AdministratorEntity();
            administratorEntity.setUserAccount(userAccountEntity);
            administratorEntity.setId(null);
            administratorEntity = administratorRepository.saveAndFlush(administratorEntity);
        } else {
            throw new BadRequestException(UserAccountService.class.getName());
        }
        return modelMapper.map(userAccountEntity, User.class);
    }

    @Override
    public List<User> getAllAccounts() {
        List<UserAccountEntity> userAccountEntities = userAccountRepository.findAll();

        return userAccountEntities.stream().map((entity) -> modelMapper.map(entity, User.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean changePassword(PasswordChange passwordChange) throws NotFoundException, UnauthorizedException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(passwordChange.getUserAccountId())
                .orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
        if (passwordEncoder.matches(passwordChange.getCurrentPassword(), userAccountEntity.getPassword())) {
            userAccountEntity.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
            userAccountRepository.save(userAccountEntity);
            return true;
        }
        return false;
    }

    @Override
    public UserAccountEntity confirmAccount(Integer id) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));

        if (userAccountEntity == null)
            throw new NotFoundException(UserAccountService.class.getName());

        if (!userAccountEntity.isConfirmed()) {
            userAccountEntity.setConfirmed(true);
            userAccountRepository.save(userAccountEntity);

            emailService.sendEmail(userAccountEntity.getEmail(), subject, body);
        }
        return userAccountEntity;
    }

    @Override
    public User getUserByUserAccountId(Integer id) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
        User user = modelMapper.map(userAccountEntity, User.class);
        return user;
    }

    @Override
    public boolean blockUserAccount(Integer id) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
        userAccountEntity.setStatus("block");
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        if (userAccountEntity.getStatus().equals("block"))
            return true;
        else
            return false;
    }

    @Override
    public boolean suspendUserAccount(Integer id) throws NotFoundException, BadRequestException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
        if ("block".equals(userAccountEntity.getStatus())) {
            throw new BadRequestException(UserAccountService.class.getName());
        }
        userAccountEntity.setStatus("suspended");
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        if (userAccountEntity.getStatus().equals("suspended"))
            return true;
        else
            return false;
    }

    public boolean unsuspendUserAccount(Integer id) throws NotFoundException, BadRequestException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
        if ("block".equals(userAccountEntity.getStatus())) {
            throw new BadRequestException(UserAccountService.class.getName());
        }
        userAccountEntity.setStatus("active");
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);
        if (userAccountEntity.getStatus().equals("active"))
            return true;
        else
            return false;
    }

    @Override
    public UserAccountResponse createUserAccount(UserAccount userAccount) throws NotFoundException {
        UserAccountEntity userAccountEntity = modelMapper.map(userAccount, UserAccountEntity.class);
        userAccountEntity.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccountEntity.setId(null);
        userAccountEntity.setStatus("active");
        userAccountEntity.setCreatedAt(new Date());
        userAccountEntity.setConfirmed(false);
        userAccountEntity = userAccountRepository.saveAndFlush(userAccountEntity);

        if (userAccount.getRole().equals("manager")) {
            ManagerEntity managerEntity = new ManagerEntity();
            managerEntity.setUserAccount(userAccountEntity);
            managerEntity.setId(null);
            managerEntity = managerRepository.saveAndFlush(managerEntity);

        } else {
            GuestEntity guestEntity = new GuestEntity();
            guestEntity.setUserAccount(userAccountEntity);
            guestEntity.setId(null);
            guestEntity = guestRepository.saveAndFlush(guestEntity);
        }

        Optional<UserAccountEntity> optionalManager = userAccountRepository.findById(userAccountEntity.getId());
        return optionalManager.map(manager -> modelMapper.map(manager, UserAccountResponse.class))
                .orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));
    }

    @Override
    public User login(LoginUser loginUser) throws NotFoundException, UnauthorizedException {
        UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(loginUser.getUsername());
        if (userAccountEntity != null && "active".equals(userAccountEntity.getStatus())
                && userAccountEntity.isConfirmed()) {
            if (passwordEncoder.matches(loginUser.getPassword(), userAccountEntity.getPassword())) {
                return modelMapper.map(userAccountEntity, User.class);
            } else {
                throw new UnauthorizedException(UserAccountService.class.getName());
            }
        } else {
            throw new NotFoundException(UserAccountService.class.getName());
        }
    }

    @Override
    public boolean updateUserAccount(UserUpdateDTO updateUser) throws NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(updateUser.getUserAccountId())
                .orElseThrow(() -> new NotFoundException(UserAccountService.class.getName()));

        userAccountEntity.setUsername(updateUser.getUsername());
        userAccountEntity.setEmail(updateUser.getEmail());
        userAccountEntity.setFirstName(updateUser.getFirstName());
        userAccountEntity.setLastName(updateUser.getLastName());
        userAccountEntity.setDateOfBirth(updateUser.getDateOfBirth());

        userAccountRepository.saveAndFlush(userAccountEntity);
        return true;
    }
}
