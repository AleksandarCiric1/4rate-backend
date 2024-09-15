package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.exceptions.UnauthorizedException;
import com.example.backend4rate.models.dto.LoginUser;
import com.example.backend4rate.models.dto.UpdateInformation;
import com.example.backend4rate.models.dto.User;
import com.example.backend4rate.models.dto.PasswordChange;
import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.models.dto.UserAccountResponse;
import com.example.backend4rate.models.dto.UserUpdateDTO;
import com.example.backend4rate.services.impl.UserAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1/userAccounts")
public class UserAccountController {
    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> registerUser(@RequestBody UserAccount userAccount) throws NotFoundException {
        return ResponseEntity.ok().body(userAccountService.createUserAccount(userAccount));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUser loginUser) throws NotFoundException, UnauthorizedException {
        return ResponseEntity.ok().body(userAccountService.login(loginUser));
    }

    @PutMapping("/passwordChange")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChange passwordChange)
            throws NotFoundException, UnauthorizedException {
        if (userAccountService.changePassword(passwordChange)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/getAllAccounts")
    public List<User> getAllAccounts() {
        return userAccountService.getAllAccounts();
    }

    @GetMapping("/getUser/{userAccountId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userAccountId) throws NotFoundException {
        return ResponseEntity.ok().body(userAccountService.getUserByUserAccountId(userAccountId));
    }

    @PutMapping("/updateUserAccount")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDTO userUpdate) throws NotFoundException {
        return ResponseEntity.ok().body(userAccountService.updateUserAccount(userUpdate));
    }

}
