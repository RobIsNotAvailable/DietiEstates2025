package com.dd25.dietiestates25;

import com.dd25.dietiestates25.dto.request.CreateCompanyAccountRequest;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.model.CompanyAccount;
import com.dd25.dietiestates25.model.enums.SecurityLevel;
import com.dd25.dietiestates25.repository.CompanyAccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;
import com.dd25.dietiestates25.service.CompanyAccountService;
import com.dd25.dietiestates25.service.utilityservice.EmailService;
import com.dd25.dietiestates25.service.utilityservice.JwtService;
import com.dd25.dietiestates25.util.SecurityUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private JwtService jwtService;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private CompanyAccountService service;


    @Test
    void testCreateCompanySuccessTC1() 
    {
        // New account
        String email = "john.doe@example.com";
        String firstName = "John";
        String lastName = "Agent";
        SecurityLevel securityLevel = SecurityLevel.AGENT;
        CreateCompanyAccountRequest request = new CreateCompanyAccountRequest(email, firstName, lastName, securityLevel);

        // Requester
        String requesterEmail = "requester@example.com";
        // String requesterFirstName = "John";
        // String requesterLastName = "Requester";
        // String requesterPassword = "notRelevant123";
        // SecurityLevel requesterSecurityLevel = SecurityLevel.ADMIN;
        // CompanyAccount requester = new CompanyAccount(requesterEmail, requesterFirstName, requesterLastName, requesterPassword, requesterSecurityLevel);

        // Extra
        LoginToken token = new LoginToken(email);

        Mockito.when(securityUtil.getCurrentEmail()).thenReturn(requesterEmail);
        // checkRolePermission is okay
        Mockito.when(repo.findById(email)).thenReturn(Optional.empty());
        Mockito.when(new LoginToken(email)).thenReturn(token);

        service.createCompanyAccount(request);

        Mockito.verify(repo).findById(email);
        Mockito.verify(emailService).sendOnboardingEmail(email, token.getToken());

        ArgumentCaptor<CompanyAccount> captor = ArgumentCaptor.forClass(CompanyAccount.class);
        // ArgumentCaptor<LoginToken> tokenCaptor = ArgumentCaptor.forClass(LoginToken.class);
        
        // Mockito.verify(repo).save(captor.capture());
        // Mockito.verify(tokenRepo).save(tokenCaptor.capture());

        CompanyAccount savedAccount = captor.getValue(); 
        
        assertEquals(email, savedAccount.getEmail());
        assertEquals(firstName, savedAccount.getFirstName());
        assertEquals(lastName, savedAccount.getLastName());
    }


//     @Test  
//     void testLoginSuccessTC1() 
//     {
//         String email = "john.doe@example.com";
//         String password = "Password123!";
//         String encodedPassword = "encoded_password_abc";

//         Client client = new Client(email, "John", "Doe", encodedPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(password, encodedPassword)).thenReturn(true);

//         LoginRequest request = new LoginRequest(email, password);

//         accountService.login(request);

//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(password, encodedPassword);
//     }

//     @Test
//     void testLoginInvalidPasswordTC2()
//     {
//         String email = "john.doe@example.com";
//         String wrongPassword = "WrongPassword123!";
//         String correctEncodedPassword = "encoded_password_abc";
        
//         LoginRequest request = new LoginRequest(email, wrongPassword);
        
//         Client client = new Client(email, "John", "Doe", correctEncodedPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(wrongPassword, correctEncodedPassword)).thenReturn(false);

//         assertThrows(RuntimeException.class, () -> 
//         {
//             accountService.login(request);
//         });

//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(wrongPassword, correctEncodedPassword);
//     }

//     @Test
//     void testLoginEmailNotFoundTC3()
//     {
//         String email = "nonexistent@example.com";
//         String password = "Password123!";
        
//         LoginRequest request = new LoginRequest(email, password);
        
//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.empty());

//         assertThrows(RuntimeException.class, () -> 
//         {
//             accountService.login(request);
//         });

//         Mockito.verify(repo).findById(email);
//     }

//     @Test
//     void testLoginInvalidEmailTC4()
//     {
//         String email = "nonexistent@example.com";
//         String password = "Password123!";
        
//         LoginRequest request = new LoginRequest(email, password);
        
//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.empty());

//         assertThrows(RuntimeException.class, () -> 
//         {
//             accountService.login(request);
//         });

//         Mockito.verify(repo).findById(email);
//     }

//     @Test
//     void testChangePasswordSuccessTC1()
//     {
//         String email = "john.doe@example.com";
//         String oldPassword = "OldPassword123!";
//         String newPassword = "NewPassword123!";
//         String encodedOldPassword = "encoded_old_password";
//         String encodedNewPassword = "encoded_new_password";

//         ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
//         Client client = new Client(email, "John", "Doe", encodedOldPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);
//         Mockito.when(encoder.encode(newPassword)).thenReturn(encodedNewPassword);

//         accountService.changePassword(request);

//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(oldPassword, encodedOldPassword);
//         Mockito.verify(encoder).encode(newPassword);

//         assertEquals(encodedNewPassword, client.getHashPassword());
//     }

//     @Test
//     void testChangePasswordIncorrectOldPasswordTC2()
//     {
//         String email = "john.doe@example.com";
//         String oldPassword = "WrongOldPassword123!";
//         String newPassword = "NewPassword123!";
//         String encodedOldPassword = "encoded_old_password";

//         ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
//         Client client = new Client(email, "John", "Doe", encodedOldPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(oldPassword, encodedOldPassword)).thenReturn(false);

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
//         {
//             accountService.changePassword(request);
//         });

//         assertEquals("Old password is incorrect", exception.getMessage());
//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(oldPassword, encodedOldPassword);
//         Mockito.verify(encoder, Mockito.never()).encode(Mockito.anyString());
//     }

//     @Test
//     void testChangePasswordSameAsOldPasswordTC3()
//     {
//         String email = "john.doe@example.com";
//         String oldPassword = "SamePassword123!";
//         String newPassword = "SamePassword123!";
//         String encodedOldPassword = "encoded_old_password";

//         ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
//         Client client = new Client(email, "John", "Doe", encodedOldPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);

//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
//         {
//             accountService.changePassword(request);
//         });

//         assertEquals("New password must be different from the old password", exception.getMessage());
//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(oldPassword, encodedOldPassword);
//         Mockito.verify(encoder, Mockito.never()).encode(Mockito.anyString());
//     }

//     @Test
//     void testChangePasswordWeakNewPasswordTC4()
//     {
//         String email = "john.doe@example.com";
//         String oldPassword = "OldPassword123!";
//         String newPassword = "weak";
//         String encodedOldPassword = "encoded_old_password";

//         ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
//         Client client = new Client(email, "John", "Doe", encodedOldPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
//         Mockito.when(encoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);

//         // Qui l'eccezione viene lanciata da validatePassword(newPassword)
//         assertThrows(IllegalArgumentException.class, () -> 
//         {
//             accountService.changePassword(request);
//         });

//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder).matches(oldPassword, encodedOldPassword);
//         Mockito.verify(encoder, Mockito.never()).encode(Mockito.anyString());
//     }

//     @Test
//     void testChangePasswordAccountNotFoundTC5()
//     {
//         String email = "nonexistent@example.com";
//         String oldPassword = "OldPassword123!";
//         String newPassword = "NewPassword123!";

//         ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);

//         Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.empty());

//         IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
//         {
//             accountService.changePassword(request);
//         });

//         assertEquals("Account not found", exception.getMessage());
//         Mockito.verify(repo).findById(email);
//         Mockito.verify(encoder, Mockito.never()).matches(Mockito.anyString(), Mockito.anyString());
//         Mockito.verify(encoder, Mockito.never()).encode(Mockito.anyString());
//     }
}