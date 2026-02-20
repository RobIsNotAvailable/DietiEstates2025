package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.service.CompanyAccountService;

@RestController
@RequestMapping("/api/company")
public class CompanyAccountController 
{
    private final CompanyAccountService companyService;

    public CompanyAccountController(CompanyAccountService companyService)
    {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCompanyAccount(@RequestBody @Valid CreateCompanyAccountRequest request) 
    {
        companyService.createCompanyAccount(request);
        return ResponseEntity.ok("Account created successfully");
    }
}
