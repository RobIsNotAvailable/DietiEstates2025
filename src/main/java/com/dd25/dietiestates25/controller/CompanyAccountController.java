package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.dto.TokenLoginRequest;
import com.dd25.dietiestates25.service.CompanyAccountService;
import com.dd25.dietiestates25.service.AccountService;

@RestController
@RequestMapping("/api/company_accounts")
public class CompanyAccountController 
{
    private final AccountService accountService;
    private final CompanyAccountService companyService;

    public CompanyAccountController(AccountService accountService, CompanyAccountService companyService)
    {
        this.accountService = accountService;
        this.companyService = companyService;
    }

    @PostMapping("/create_company_account")
    public ResponseEntity<String> createCompanyAccount
    (@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid CreateCompanyAccountRequest request) 
    {
        companyService.createCompanyAccount(requesterEmail, request);
        return ResponseEntity.ok("Account created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) 
    {
        accountService.login(request);
        return ResponseEntity.ok("Login successful");
    }
    
    @PatchMapping("/change_password")
    public ResponseEntity<String> changePassword
    (@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid ChangePasswordRequest request) 
    {
        accountService.changePassword(requesterEmail, request);
        return ResponseEntity.ok("Password succesfully updated");
    }

    @PatchMapping("/link_login/{token}")
    public ResponseEntity<String> tokenLogin(@PathVariable @NonNull String token, @RequestBody @Valid TokenLoginRequest request)
    {
        accountService.tokenLogin(token, request);
        return ResponseEntity.ok("Password set successfully");
    }
}
