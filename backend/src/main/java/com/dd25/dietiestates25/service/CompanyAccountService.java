package com.dd25.dietiestates25.service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.request.AgentStatsRequest;
import com.dd25.dietiestates25.dto.request.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.dto.response.AgentMonthlyStatsResponse;
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

    public AgentMonthlyStatsResponse getStatsForSelectedMonth(AgentStatsRequest request) 
    {
        System.out.println("--- [DEBUG START] getStatsForSelectedMonth ---");
        System.out.println("EMAIL RICEVUTA: [" + request.agentEmail() + "]");
        System.out.println("MESE/ANNO: " + request.month() + "/" + request.year());

        YearMonth selectedMonth = YearMonth.of(request.year(), request.month());

        OffsetDateTime startOfMonth = selectedMonth.atDay(1)
                                                .atStartOfDay()
                                                .atOffset(ZoneOffset.UTC);

        OffsetDateTime startOfNextMonth = selectedMonth.plusMonths(1)
                                                    .atDay(1)
                                                    .atStartOfDay()
                                                    .atOffset(ZoneOffset.UTC);

        System.out.println("RANGE DATE CALCOLATO: " + startOfMonth + " -> " + startOfNextMonth);

        // Eseguiamo la query
        AgentMonthlyStatsResponse response = repo.getStatsForSelectedMonth(
                request.agentEmail(), 
                startOfMonth, 
                startOfNextMonth
        );

        // Logghiamo cosa ha sputato fuori il database tramite JPA
        if (response != null) 
        {
            System.out.println("DATI DAL REPO:");
            System.out.println(">> Active Listings: " + response.activeListings());
            System.out.println(">> Total Views: " + response.nViews());
            System.out.println(">> Active Visits: " + response.activeVisits());
        } 
        else 
        {
            System.out.println("ERRORE: Il repository ha restituito NULL!");
        }
        System.out.println("--- [DEBUG END] ---");

        return response;
    }
    private void checkRolePermission(CompanyAccount requester, SecurityLevel targetLevel) 
    {
        SecurityLevel requesterLevel = requester.getSecurityLevel();

        boolean isAllowed = false;

        if (requesterLevel == SecurityLevel.ADMIN)
            isAllowed = (targetLevel != SecurityLevel.ADMIN);
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

