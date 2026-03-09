package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dd25.dietiestates25.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.dd25.dietiestates25.dto.request.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.request.ResetPasswordRequest;
import com.dd25.dietiestates25.dto.response.AccountDetailsResponse;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AccountController 
{
    private final AccountService accountService;

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

    @GetMapping("/me")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails() 
    {
        AccountDetailsResponse response = accountService.getAccountDetails();
        return ResponseEntity.ok(response);
    }
    
}