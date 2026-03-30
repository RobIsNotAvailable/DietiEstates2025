package com.dd25.dietiestates25;

import com.dd25.dietiestates25.dto.request.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;
import com.dd25.dietiestates25.service.CompanyAccountService;
import com.dd25.dietiestates25.service.utilityservice.EmailService;
import com.dd25.dietiestates25.util.SecurityUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;



@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class CompanyServiceTest 
{
    @Mock
    private CompanyAccountRepository repo;

    @Mock
    private LoginTokenRepository tokenRepo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CompanyAccountService service;


    private String goodEmail = "john.new@example.com";
    private String goodName = "John";
    private String goodLastName = "New";
    private SecurityLevel goodSecurityLevel = SecurityLevel.AGENT;

    private String creatorEmail = "creator@example.com";
    private String creatorFirstName = "John";
    private String creatorLastName = "creator";
    private String creatorPassword = "notRelevant123";
    private SecurityLevel creatorSecurityLevel = SecurityLevel.SUPPORT;

    @Test
    void testCreateCompanySuccessTC1() 
    {
        CreateCompanyAccountRequest request = new CreateCompanyAccountRequest(goodEmail, goodName, goodLastName, goodSecurityLevel);
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());
        Mockito.when(repo.findById(creator.getEmail())).thenReturn(Optional.of(creator));
        Mockito.when(repo.findById(request.email())).thenReturn(Optional.empty());

        service.createCompanyAccount(request);

        ArgumentCaptor<CompanyAccount> captor = ArgumentCaptor.forClass(CompanyAccount.class);
        ArgumentCaptor<LoginToken> tokenCaptor = ArgumentCaptor.forClass(LoginToken.class);

        Mockito.verify(repo).save(captor.capture());
        Mockito.verify(tokenRepo).save(tokenCaptor.capture());

        Mockito.verify(repo).findById(request.email());
        Mockito.verify(emailService).sendOnboardingEmail(request.email(), tokenCaptor.getValue().getToken());

        CompanyAccount savedAccount = captor.getValue(); 
        
        assertEquals(request.email(), savedAccount.getEmail());
        assertEquals(request.firstName(), savedAccount.getFirstName());
        assertEquals(request.lastName(), savedAccount.getLastName());
    }

    @Test
    void testCreateCompanyAlreadyRegisteredTC2() 
    {
        String registeredEmail = "already@registered.com";
        CreateCompanyAccountRequest request = new CreateCompanyAccountRequest(registeredEmail, goodName, goodLastName, goodSecurityLevel);

        CompanyAccount existent = new CompanyAccount(registeredEmail, goodName, goodLastName, "Notrelevant123", goodSecurityLevel);
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());
        Mockito.when(repo.findById(creator.getEmail())).thenReturn(Optional.of(creator));
        Mockito.when(repo.findById(request.email())).thenReturn(Optional.of(existent));

        assertThrows(IllegalStateException.class, () -> 
        {
            service.createCompanyAccount(request);
        });

        Mockito.verify(repo).findById(registeredEmail);
    }

    @Test
    void testCreateCompanyCreatorNotFoundTC3() 
    {
        String nonAgentEmail = "impostor@wrong.sus";
        CreateCompanyAccountRequest request = new CreateCompanyAccountRequest(goodEmail, goodName, goodLastName, goodSecurityLevel);
        CompanyAccount creator = new CompanyAccount(nonAgentEmail, creatorFirstName, creatorLastName, creatorPassword, creatorSecurityLevel);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());
        Mockito.when(repo.findById(creator.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.createCompanyAccount(request);
        });

        Mockito.verify(repo).findById(creator.getEmail());
    }

    @Test
    void testCreateCompanyCreatorTooWeakTC4() 
    {
        CreateCompanyAccountRequest request = new CreateCompanyAccountRequest(goodEmail, goodName, goodLastName, goodSecurityLevel);
        CompanyAccount creator = new CompanyAccount(creatorEmail, creatorFirstName, creatorLastName, creatorPassword, SecurityLevel.AGENT);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(creator.getEmail());
        Mockito.when(repo.findById(creator.getEmail())).thenReturn(Optional.of(creator));

        assertThrows(SecurityException.class, () -> 
        {
            service.createCompanyAccount(request);
        });

        Mockito.verify(repo).findById(creator.getEmail());
    }
}