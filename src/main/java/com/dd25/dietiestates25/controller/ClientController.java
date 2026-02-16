package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.service.AccountService;
import com.dd25.dietiestates25.service.ClientService;

import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;

@RestController
@RequestMapping("/api/clients")
public class ClientController 
{
    private final AccountService accountService;
    private final ClientService clientService;

    public ClientController(AccountService accountService, ClientService clientService)
    {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AccountRegisterRequest request) 
    {    
        clientService.registerClient(request);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) 
    {
        accountService.login(request);
        return ResponseEntity.ok("Login successful");
    }

    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid ChangePasswordRequest request) 
    {
        accountService.changePassword(requesterEmail, request);
        return ResponseEntity.ok("Password succesfully updated");
    }
}