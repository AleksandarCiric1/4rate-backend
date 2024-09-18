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
import com.example.backend4rate.models.dto.PasswordChange;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.models.dto.UserUpdateDTO;
import com.example.backend4rate.models.entities.UserAccountEntity;

public interface UserAccountServiceInterface {

    List<User> getAllAccounts();

    boolean changePassword(PasswordChange passwoedChange) throws NotFoundException, UnauthorizedException;

    UserAccountEntity confirmAccount(Integer id) throws NotFoundException;

    boolean blockUserAccount(Integer id) throws NotFoundException;

    boolean suspendUserAccount(Integer id) throws NotFoundException, BadRequestException;

    boolean unsuspendUserAccount(Integer id) throws NotFoundException, BadRequestException;

    UserAccountResponse createUserAccount(UserAccount userAccount) throws NotFoundException;

    User createAdministratorAccount(UserAccount userAccount) throws NotFoundException, BadRequestException;

    User login(LoginUser loginUser) throws NotFoundException, UnauthorizedException;

    User getUserByUserAccountId(Integer id) throws NotFoundException;

    boolean updateUserAccount(UserUpdateDTO updateUser) throws NotFoundException;
}
