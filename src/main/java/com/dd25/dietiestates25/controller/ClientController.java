package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.service.ClientService;

import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;

@RestController
@RequestMapping("/api/clients")
public class ClientController 
{
    private final ClientService service;

    public ClientController(ClientService service)
    {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid AccountRegisterRequest request) 
    {    
        service.registerClient(requesterEmail, request.firstName(), request.lastName(), request.password());
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) 
    {
        service.login(request.email(), request.password());
        return ResponseEntity.ok("Login successful");
    }

    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword(@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid ChangePasswordRequest request) 
    {
        service.changePassword(requesterEmail, request.oldPassword(), request.newPassword());
        return ResponseEntity.ok("Password succesfully updated");
    }
}