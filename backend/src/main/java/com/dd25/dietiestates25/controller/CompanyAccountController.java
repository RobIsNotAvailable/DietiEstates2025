package com.dd25.dietiestates25.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.dd25.dietiestates25.dto.request.AgentStatsRequest;
import com.dd25.dietiestates25.dto.request.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.dto.response.AgentStatsResponse;
import com.dd25.dietiestates25.dto.response.StringResponse;
import com.dd25.dietiestates25.service.CompanyAccountService;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyAccountController 
{
    private final CompanyAccountService companyService;

    @PostMapping("/create")
    public ResponseEntity<StringResponse> create(@RequestBody @Valid CreateCompanyAccountRequest request) 
    {
        companyService.createCompanyAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new StringResponse("Account created successfully"));
    }

    @PostMapping("/stats")
    public ResponseEntity<AgentStatsResponse> getMonthlyStats(@RequestBody @Valid AgentStatsRequest request) 
    {   
        AgentStatsResponse stats = companyService.getStatsForSelectedMonth(request);
        
        return ResponseEntity.ok(stats);
    }
}