package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.model.enums.SecurityLevel;
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

    @PostMapping("/create_support_account")
    public ResponseEntity<String> createSupportAccount
    (
        @RequestHeader("Requester_Email") String requesterEmail,
        @RequestParam @NonNull String email,
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam SecurityLevel securityLevel
    ) 
    {
        if(requesterEmail == null || requesterEmail.isEmpty()) 
            return ResponseEntity.status(400).body("Bad Request: Requester email is required.");
        
        service.createCompanyAccount(requesterEmail, email, firstName, lastName, securityLevel);
        
        return ResponseEntity.ok("Account created successfully");
    }
}
