package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.UserAccount;

public interface UserAccountServiceInterface {
    boolean updateInformation(UserAccount userAccount);

    UserAccount getInformation();

    List<UserAccount> getAllUserAccount();

    UserAccount getUserAccountById(Integer id) throws NotFoundException;

    boolean deleteUserAccount(Integer id);

    boolean suspendUserAccount(Integer id);

    UserAccount createManagerAccount(UserAccount userAccount) throws NotFoundException;

    boolean login(LoginUser loginUser);
}
