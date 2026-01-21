package com.dd25.dietiestates25.service;

import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.Enums.SecurityLevel;
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

    public void CreateCompanyAccount(@NonNull String email, String firstName, String lastName, String companyName, SecurityLevel securityLevel)
    {
        String defaultPassword = "ChangeMe123";

        CompanyAccount company = new CompanyAccount(email, firstName, lastName, companyName, defaultPassword, securityLevel);
        
        if (repo.findById(email).isPresent())
        {
            throw new IllegalStateException("Email already registered");
        }

        repo.save(company);
    }

    public boolean canManageRole(@NonNull String requesterEmail, SecurityLevel targetLevel) 
    {
        Optional<CompanyAccount> requesterOpt = repo.findById(requesterEmail);

        if (requesterOpt.isEmpty()) 
            return false;
        

        SecurityLevel requesterLevel = requesterOpt.get().getSecurityLevel();

        if (requesterLevel == SecurityLevel.ADMIN)
            return true; 

        if (requesterLevel == SecurityLevel.SUPPORT) 
            return targetLevel == SecurityLevel.AGENT;

        return false;
    }


    @Transactional
    public void changePassword(@NonNull String email, String oldRawPassword, String newRawPassword)
    {
        Optional<CompanyAccount> companyAccountOptional = repo.findById(email);
        
        if (companyAccountOptional.isPresent())
        {
            CompanyAccount companyAccount = companyAccountOptional.get();
            if (!encoder.matches(oldRawPassword, companyAccount.getHashPassword()))
            {
                throw new SecurityException("Old password is incorrect");
            }

            if (!newRawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            {
                throw new IllegalArgumentException("New password must be at least 8 characters long and contain both letters and numbers");
            }

            if (encoder.matches(newRawPassword, companyAccount.getHashPassword()))
            {
                throw new IllegalArgumentException("New password must be different from the old password");
            }

            companyAccount.setHashPassword(encoder.encode(newRawPassword));

            companyAccount.setMustChangePassword(false);
        }
        else
        {
            throw new SecurityException("Company account not found");
        }
    }
}

