package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.models.dto.UserAccount;
import com.example.backend4rate.services.impl.AuthService;
import com.example.backend4rate.services.impl.ImageService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ImageService imageService;

    public AuthController(AuthService authService, ImageService imageService) {
        this.authService = authService;
        this.imageService = imageService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(){

        return ResponseEntity.ok().body("");
    }

    @PostMapping("/register")
    public String postMethodName(@RequestBody String entity) {
        
        return entity;
    }

    @GetMapping("/confirm-account")
    public String confirmAccount(@RequestParam("token") String confirmationToken) {
        

        return "";
    }

    @GetMapping("/get-accounts")
    public List<UserAccount> getAccount() {
        return this.authService.getAllUserAccounts();
    }



    @GetMapping("/hello-man")
    public String sayHello() {
        this.imageService.doSomethingTest();
        return "";
    }
    
    
}
