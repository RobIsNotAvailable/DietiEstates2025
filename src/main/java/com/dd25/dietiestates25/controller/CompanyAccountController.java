package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.service.CompanyAccountService;

@RestController
@RequestMapping("/api/company_accounts")
public class CompanyAccountController 
{
    private final CompanyAccountService service;

    public CompanyAccountController(CompanyAccountService service)
    {
        this.service = service;
    }

    @PostMapping("/create_company_account")
    public ResponseEntity<String> createCompanyAccount
    (@RequestHeader("Requester-Email") @NonNull String requesterEmail, @RequestBody @Valid CreateCompanyAccountRequest request) 
    {
        service.createCompanyAccount(requesterEmail, request);
        return ResponseEntity.ok("Account created successfully");
    }
}
