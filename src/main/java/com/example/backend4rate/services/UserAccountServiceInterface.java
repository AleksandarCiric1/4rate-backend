package com.example.backend4rate.services;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.StandardUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;


public interface UserAccountServiceInterface {
    boolean updateInformation(UpdateInformation updateInformation, Integer id) throws NotFoundException;

    StandardUser getInformation(Integer id) throws NotFoundException;

    List<UserAccountResponse> getAllUserAccount();

    UserAccountResponse getUserAccountById(Integer id) throws NotFoundException;

    void deleteUserAccount(Integer id);

    boolean suspendUserAccount(Integer id)throws NotFoundException;

    boolean unsuspendUserAccount(Integer id)throws NotFoundException;

    UserAccountResponse createUserAccount(UserAccount userAccount) throws NotFoundException;

    UserAccountResponse createAdministratorAccount(UserAccount userAccount) throws NotFoundException, BadRequestException;

    UserAccountResponse login(LoginUser loginUser) throws NotFoundException, UnauthorizedException;
}
