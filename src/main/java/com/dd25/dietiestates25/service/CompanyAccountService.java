package com.dd25.dietiestates25.service;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;


@Service
public class CompanyAccountService 
{
    private final CompanyAccountRepository repo;
    private final LoginTokenRepository tokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public CompanyAccountService(CompanyAccountRepository repo, LoginTokenRepository tokenRepo, EmailService emailService)
    {
        this.repo = repo;
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
    }

    public void createCompanyAccount(@NonNull String requesterEmail, CreateCompanyAccountRequest request)
    {
        CompanyAccount requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));
        
        checkRolePermission(requester, request.securityLevel());

        String defaultPassword = generateSecurePassword();

        CompanyAccount newAccount = new CompanyAccount(request.email(), request.firstName(), request.lastName(), encoder.encode(defaultPassword), request.securityLevel());
        
        if (repo.findById(request.email()).isPresent())
            throw new IllegalStateException("Email already registered");

        LoginToken token = new LoginToken(request.email());

        repo.save(newAccount);
        tokenRepo.save(token);

        emailService.sendOnboardingEmail(request.email(), token.getToken());
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

    private String generateSecurePassword()
    {
        PasswordGenerator gen = new PasswordGenerator();

        CharacterRule letter = new CharacterRule(EnglishCharacterData.Alphabetical);
        letter.setNumberOfCharacters(1);

        CharacterRule digit = new CharacterRule(EnglishCharacterData.Digit);
        digit.setNumberOfCharacters(1);

        return gen.generatePassword(32, letter, digit);
    }
}

