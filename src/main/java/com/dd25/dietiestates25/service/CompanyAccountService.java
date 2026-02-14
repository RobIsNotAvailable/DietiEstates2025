package com.dd25.dietiestates25.service;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;

import jakarta.transaction.Transactional;

@Service
public class CompanyAccountService 
{
    private final CompanyAccountRepository repo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public CompanyAccountService(CompanyAccountRepository repo)
    {
        this.repo = repo;
    }

    public void createCompanyAccount(@NonNull String requesterEmail, CreateCompanyAccountRequest request)
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));
        
        checkRolePermission(requester, request.securityLevel());

        String defaultPassword = "ChangeMe123";

        CompanyAccount newAccount = new CompanyAccount(request.email(), request.firstName(), request.lastName(), encoder.encode(defaultPassword), request.securityLevel());
        
        if (repo.findById(request.email()).isPresent())
            throw new IllegalStateException("Email already registered");

        repo.save(newAccount);
    }

    @Transactional
    public void changePassword(@NonNull String requesterEmail, ChangePasswordRequest request)
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));

        if (!encoder.matches(request.oldPassword(), requester.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (request.oldPassword().equals(request.newPassword()))
            throw new IllegalStateException("New password must be different from the old password");

        requester.setHashPassword(encoder.encode(request.newPassword()));

        requester.setPasswordChanged(false);
    }

    private void checkRolePermission(CompanyAccount requester, SecurityLevel targetLevel) 
    {
        SecurityLevel requesterLevel = requester.getSecurityLevel();

        boolean isAllowed = false;

        if (requesterLevel == SecurityLevel.ADMIN)
            isAllowed = true;
        else if (requesterLevel == SecurityLevel.SUPPORT)
            isAllowed = (targetLevel == SecurityLevel.AGENT);

        if (!isAllowed)
            throw new SecurityException("Insufficient permissions to manage role: " + targetLevel);
    }
}

