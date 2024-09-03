package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.services.impl.UserAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/v1/userAccounts")
public class UserAccountController {
    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }

    @PostMapping("/createAccount")
    public UserAccount registerUser(@RequestBody UserAccount userAccount) throws NotFoundException{
        return userAccountService.createUserAccount(userAccount);
    }

    @PostMapping("/login")
    public UserAccount login(@RequestBody LoginUser loginUser) throws NotFoundException, UnauthorizedException{
        return userAccountService.login(loginUser);
    }

    @GetMapping("/getInformation/{id}")
    public UserAccount getInformation(@PathVariable Integer id) throws NotFoundException{
        return userAccountService.getInformation(id);
    } 

    @PutMapping("/updateInformations")
    public boolean updateInformation(@RequestBody UpdateInformation updateInformation) throws NotFoundException{
         return userAccountService.updateInformation(updateInformation);
    }
}
