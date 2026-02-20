package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.AuthResponse;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.dto.TokenLoginRequest;
import com.dd25.dietiestates25.service.AccountService;
import com.dd25.dietiestates25.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final AccountService accountService;
    private final ClientService clientService;

    public AuthController(AccountService accountService, ClientService clientService)
    {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AccountRegisterRequest request) 
    {    
        AuthResponse response = clientService.registerClient(request);
        return ResponseEntity.ok(response.token());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) 
    {
        AuthResponse response = accountService.login(request);
        return ResponseEntity.ok(response.token());
    }

    @PatchMapping("/link-login/{token}")
    public ResponseEntity<String> tokenLogin(@PathVariable @NonNull String token, @RequestBody @Valid TokenLoginRequest request)
    {
        AuthResponse response = accountService.tokenLogin(token, request);
        return ResponseEntity.ok(response.token());
    }
}
