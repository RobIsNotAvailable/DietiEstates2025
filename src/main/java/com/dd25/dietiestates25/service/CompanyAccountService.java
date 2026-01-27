package com.dd25.dietiestates25.service;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;

import jakarta.transaction.Transactional;

public class CompanyAccountService 
{
    private final CompanyAccountRepository repo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public CompanyAccountService(CompanyAccountRepository repo)
    {
        this.repo = repo;
    }

    public void createCompanyAccount(@NonNull String requesterEmail, @NonNull String email, String firstName, String lastName, SecurityLevel securityLevel)
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));
        
        checkRolePermission(requesterEmail, securityLevel);

        String defaultPassword = "ChangeMe123";

        CompanyAccount newAccount = new CompanyAccount(email, firstName, lastName, requester.getCompanyName(), defaultPassword, securityLevel);
        
        if (repo.findById(email).isPresent())
            throw new IllegalStateException("Email already registered");

        repo.save(newAccount);
    }

    public void checkRolePermission(@NonNull String requesterEmail, SecurityLevel targetLevel) 
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));
        
        SecurityLevel requesterLevel = requester.getSecurityLevel();

        boolean isAllowed = false;

        if (requesterLevel == SecurityLevel.ADMIN)
            isAllowed = true;
        else if (requesterLevel == SecurityLevel.SUPPORT)
            isAllowed = (targetLevel == SecurityLevel.AGENT);

        if (!isAllowed)
            throw new SecurityException("Insufficient permissions to manage role: " + targetLevel);
    }


    @Transactional
    public void changePassword(@NonNull String requesterEmail, String oldRawPassword, String newRawPassword)
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));;

        if (!encoder.matches(oldRawPassword, requester.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (!newRawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            throw new IllegalArgumentException("New password must be at least 8 characters long and contain both letters and numbers");

        if (encoder.matches(newRawPassword, requester.getHashPassword()))
            throw new IllegalArgumentException("New password must be different from the old password");

        requester.setHashPassword(encoder.encode(newRawPassword));

        requester.setMustChangePassword(false);

    }
}

