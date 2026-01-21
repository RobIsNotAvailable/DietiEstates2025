package com.dd25.dietiestates25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.dd25.dietiestates25.model.Enums.SecurityLevel;
import com.dd25.dietiestates25.service.CompanyAccountService;

@RestController
@RequestMapping("/api/company-accounts")
public class CompanyAccountController 
{
    private final CompanyAccountService service;

    public CompanyAccountController(CompanyAccountService service)
    {
        this.service = service;
    }

    @PostMapping("/create-support-account")
    public ResponseEntity<String> createSupportAccount
    (
        @RequestHeader("Requester-Email") String requesterEmail, 
        @RequestParam @NonNull String email, 
        @RequestParam String firstName, 
        @RequestParam String lastName, 
        @RequestParam String companyName, 
        @RequestParam String password,
        @RequestParam SecurityLevel securityLevel
    ) 
    {
        if(requesterEmail == null || requesterEmail.isEmpty()) 
            return ResponseEntity.status(400).body("Bad Request: Requester email is required.");

        boolean isAuthorized = service.canManageRole(requesterEmail, securityLevel);
        
        if (!isAuthorized) 
            return ResponseEntity.status(403).body("Denied: Insufficient permissions to create support account.");
        

        service.CreateCompanyAccount(email, firstName, lastName, companyName, securityLevel);
        
        return ResponseEntity.ok("account created successfully");
    }
}
