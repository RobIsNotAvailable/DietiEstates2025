package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.service.AccountService;

import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.ResetPasswordRequest;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*") 
public class AccountController 
{
    private final AccountService accountService;

    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) 
    {
        accountService.changePassword(request);
        return ResponseEntity.ok("Password successfully updated");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ResetPasswordRequest request)
    {
        accountService.forgotPassword(request);
        return ResponseEntity.ok("Password reset link sent to email");
    }
}