package com.example.backend4rate.services;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.StandardUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.User;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.models.entities.UserAccountEntity;


public interface UserAccountServiceInterface {
    boolean updateInformation(UpdateInformation updateInformation, Integer id) throws NotFoundException;

    StandardUser getInformation(Integer id) throws NotFoundException;

    List<UserAccountResponse> getAllUserAccount();
    
    List<User> getAllAccounts();

    void changePassword(LoginUser loginUser, String password) throws NotFoundException, UnauthorizedException;

    UserAccountEntity confirmAccount(Integer id) throws NotFoundException;

    UserAccountResponse getUserAccountById(Integer id) throws NotFoundException;

    boolean blockUserAccount(Integer id) throws NotFoundException;

    boolean suspendUserAccount(Integer id)throws NotFoundException, BadRequestException;

    boolean unsuspendUserAccount(Integer id)throws NotFoundException, BadRequestException;

    UserAccountResponse createUserAccount(UserAccount userAccount) throws NotFoundException;

    User createAdministratorAccount(UserAccount userAccount) throws NotFoundException, BadRequestException;

    UserAccountResponse login(LoginUser loginUser) throws NotFoundException, UnauthorizedException;
}
