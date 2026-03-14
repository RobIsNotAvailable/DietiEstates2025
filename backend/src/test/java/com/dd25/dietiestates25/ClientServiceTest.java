package com.dd25.dietiestates25;

import com.dd25.dietiestates25.dto.request.AccountRegisterRequest;
import com.dd25.dietiestates25.model.Account;
import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.AccountRepository;
import com.dd25.dietiestates25.service.ClientService;
import com.dd25.dietiestates25.service.utilityservice.JwtService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;



@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null") 
class ClientServiceTest 
{
    @Mock
    private AccountRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ClientService clientService;


    @Test
    void testRegisterClientSuccessTC1() 
    {
        String firstName = "John";
        String lastName = "Doe";
        String rawPassword = "Password123!";
        String encodedPassword = "encoded_password_abc";
        String email = "john.doe@example.com";
        String token = "mock_token";

        AccountRegisterRequest request = new AccountRegisterRequest(email, firstName, lastName, rawPassword);

        Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.empty());
        Mockito.when(encoder.encode(rawPassword)).thenReturn(encodedPassword);
        Mockito.when(jwtService.generateToken(any(Account.class))).thenReturn(token);

        clientService.registerClient(request);

        Mockito.verify(repo).findById(email);
        Mockito.verify(encoder).encode(rawPassword);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        
        Mockito.verify(repo).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue(); 
        
        assertEquals(email, savedClient.getEmail());
        assertEquals(firstName, savedClient.getFirstName());
        assertEquals(lastName, savedClient.getLastName());
        assertEquals(encodedPassword, savedClient.getHashPassword());
    }

    @Test
    void testRegisterClientEmailAlreadyRegisteredTC2() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, "John", "Doe", "Password123!");
        Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(new Client(email, "John", "Doe", "encoded_password")));

        assertThrows(IllegalStateException.class, () -> 
        {
            clientService.registerClient(request);
        });
    }

    @Test
    void testRegisterClientNullEmailTC3() 
    {
        AccountRegisterRequest request = new AccountRegisterRequest(null, "John", "Doe", "Password123!");

        assertThrows(NullPointerException.class, () -> 
        {
            clientService.registerClient(request);
        });
    }

    @Test
    void testRegisterClientWeakPasswordTC4() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, "John", "Doe", "weakpass");
        
        assertThrows(MethodArgumentNotValidException.class, () -> 
        {
            clientService.registerClient(request);
        });
    }    

    @Test
    void testRegisterClientFirstNameNullTC5() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, null, "Doe", "Password123!");
        
        assertThrows(NullPointerException.class, () -> 
        {
            clientService.registerClient(request);
        });
    }

    @Test
    void testRegisterClientLastNameNullTC6() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, "john", null, "Password123!");
        
        assertThrows(NullPointerException.class, () -> 
        {
            clientService.registerClient(request);
        });
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