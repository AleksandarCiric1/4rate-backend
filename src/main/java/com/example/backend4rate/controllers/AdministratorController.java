package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.services.impl.UserAccountService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/admin")
public class AdministratorController {
    UserAccountService userAccountService;

    public AdministratorController(UserAccountService userAccountService){
        this.userAccountService = userAccountService;
    }


    @DeleteMapping("/delete/{id}")
    public boolean deleteUserAccount(@PathVariable Integer id){
       return userAccountService.deleteUserAccount(id); 
    }

    @PutMapping("/suspend/{id}")
    public boolean suspendUserAccount(@PathVariable Integer id) throws NotFoundException{
        return userAccountService.suspendUserAccount(id);
    }

    @PutMapping("/unsuspend/{id}")
    public boolean unsuspendUserAccount(@PathVariable Integer id)throws NotFoundException{
        return userAccountService.unsuspendUserAccount(id);
    }

    @GetMapping
    public List<UserAccount> getAllUserAccounts(){
        return userAccountService.getAllUserAccount();
    }

    @GetMapping("/getById/{id}")
    public UserAccount getUserAccountsById(@PathVariable Integer id) throws NotFoundException{
        return userAccountService.getUserAccountById(id);
    }
}
