package com.dd25.dietiestates25.service;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.AgentStatsResponse;
import com.dd25.dietiestates25.dto.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;
import com.dd25.dietiestates25.service.utilityservice.EmailService;
import com.dd25.dietiestates25.util.SecurityUtil;
import com.dd25.dietiestates25.util.StringConstants;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CompanyAccountService 
{
    private final CompanyAccountRepository repo;
    private final LoginTokenRepository tokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final SecurityUtil securityUtil;

    public void createCompanyAccount(CreateCompanyAccountRequest request)
    {
        CompanyAccount requester = repo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException(StringConstants.ACCOUNT_NOT_FOUND_MESSAGE));
        
        checkRolePermission(requester, request.securityLevel());

        String defaultPassword = generateSecurePassword();

        CompanyAccount newAccount = new CompanyAccount(request.email(), request.firstName(), request.lastName(), encoder.encode(defaultPassword), request.securityLevel());
        
        if (repo.findById(request.email()).isPresent())
            throw new IllegalStateException(StringConstants.EMAIL_ALREADY_REGISTERED_MESSAGE);

        LoginToken token = new LoginToken(request.email());

        repo.save(newAccount);
        tokenRepo.save(token);

        emailService.sendOnboardingEmail(request.email(), token.getToken());
    }

    public AgentStatsResponse getStats()
    {
        return repo.getStatsByAgent(securityUtil.getCurrentEmail());
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

