package com.dd25.dietiestates25;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dd25.dietiestates25.dto.request.ChangePasswordRequest;
import com.dd25.dietiestates25.model.Account;
import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.AccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;
import com.dd25.dietiestates25.service.AccountService;
import com.dd25.dietiestates25.service.utilityservice.EmailService;
import com.dd25.dietiestates25.service.utilityservice.JwtService;
import com.dd25.dietiestates25.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class AccountServiceTest
{    
    @Mock
    private AccountRepository repo;
    
    @Mock
    private LoginTokenRepository tokenRepo;
    
    @Mock
    private PasswordEncoder encoder;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private JwtService jwtService;
    
    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private AccountService service;

    String email = "its@me.mario";
    String name = "mario";
    String lastName = "brossi";
    String hashPassword = "M1573R1050";

    String oldPassword = "0ldButG0ld";
    String newPassword = "miHann0RubatoID4ti";

    String newHashPassword = "lePassw0rd";

    @Test
    void testChangePasswordSuccessTC1()
    {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        Account account = new Client(email, name, lastName, hashPassword);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(account.getEmail());
        Mockito.when(repo.findById(account.getEmail())).thenReturn(Optional.of(account));
        Mockito.when(encoder.matches(request.oldPassword(), account.getHashPassword())).thenReturn(true);
        Mockito.when(encoder.encode(request.newPassword())).thenReturn(newHashPassword);

        service.changePassword(request);

        assertEquals(account.getHashPassword(), newHashPassword);
    }

    @Test
    void testChangePasswordNoAccountFoundTC2()
    {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        Account account = new Client(email, name, lastName, hashPassword);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(account.getEmail());
        Mockito.when(repo.findById(account.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
        {
            service.changePassword(request);
        });

        Mockito.verify(repo).findById(account.getEmail());
    }

    @Test
    void testChangePasswordWrongOldTC3()
    {
        String wrongOld = "sc0rdai...";
        ChangePasswordRequest request = new ChangePasswordRequest(wrongOld, newPassword);
        Account account = new Client(email, name, lastName, hashPassword);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(account.getEmail());
        Mockito.when(repo.findById(account.getEmail())).thenReturn(Optional.of(account));
        Mockito.when(encoder.matches(request.oldPassword(), account.getHashPassword())).thenReturn(false);

        assertThrows(SecurityException.class, () -> 
        {
            service.changePassword(request);
        });

        Mockito.verify(repo).findById(account.getEmail());
    }

    @Test
    void testChangePasswordSameOldTC4()
    {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, oldPassword);
        Account account = new Client(email, name, lastName, hashPassword);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(account.getEmail());
        Mockito.when(repo.findById(account.getEmail())).thenReturn(Optional.of(account));
        Mockito.when(encoder.matches(request.oldPassword(), account.getHashPassword())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> 
        {
            service.changePassword(request);
        });

        Mockito.verify(repo).findById(account.getEmail());
    }
}
