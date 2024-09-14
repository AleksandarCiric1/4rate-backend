package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.BadRequestException;
import com.example.backend4rate.models.dto.User;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.services.impl.UserAccountService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/v1/admin")
public class AdministratorController {
    UserAccountService userAccountService;

    public AdministratorController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PutMapping("/block/{id}")
    public boolean blockUserAccount(@PathVariable Integer id) throws NotFoundException {
        return userAccountService.blockUserAccount(id);
    }

    @PutMapping("/suspend/{id}")
    public boolean suspendUserAccount(@PathVariable Integer id) throws NotFoundException, BadRequestException {
        return userAccountService.suspendUserAccount(id);
    }

    @PutMapping("/unsuspend/{id}")
    public boolean unsuspendUserAccount(@PathVariable Integer id) throws NotFoundException, BadRequestException {
        return userAccountService.unsuspendUserAccount(id);
    }

    @GetMapping
    public List<UserAccountResponse> getAllUserAccounts() {
        return userAccountService.getAllUserAccount();
    }

    @GetMapping("/getById/{id}")
    public UserAccountResponse getUserAccountsById(@PathVariable Integer id) throws NotFoundException {
        return userAccountService.getUserAccountById(id);
    }

    @PostMapping("/createAdminAccount")
    public ResponseEntity<?> createAdministratorAccount(@RequestBody UserAccount userAccount)
            throws NotFoundException, BadRequestException {
        User user = userAccountService.createAdministratorAccount(userAccount);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/confirmAccount/{id}")
    public ResponseEntity<?> confirmAccount(@PathVariable Integer id) throws NotFoundException {
        if (userAccountService.confirmAccount(id) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }

    }
}
