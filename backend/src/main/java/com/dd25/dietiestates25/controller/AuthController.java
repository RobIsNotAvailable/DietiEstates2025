package com.dd25.dietiestates25.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.dto.request.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.request.LoginRequest;
import com.dd25.dietiestates25.dto.request.TokenLoginRequest;
import com.dd25.dietiestates25.dto.response.StringResponse;
import com.dd25.dietiestates25.service.AccountService;
import com.dd25.dietiestates25.service.ClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController
{
    private final AccountService accountService;
    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<StringResponse> register(@RequestBody @Valid AccountRegisterRequest request) 
    {    
        StringResponse response = clientService.registerClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<StringResponse> login(@RequestBody @Valid LoginRequest request) 
    {
        StringResponse response = accountService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/link-login/{token}")
    public ResponseEntity<StringResponse> tokenLogin(@PathVariable @NonNull String token, @RequestBody @Valid TokenLoginRequest request)
    {
        StringResponse response = accountService.tokenLogin(token, request);
        return ResponseEntity.ok(response);
    }
}
