package com.dd25.DietiEstates25;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.ClientRepository;
import com.dd25.dietiestates25.service.ClientService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest 
{
    @Mock
    private ClientRepository repo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testRegisterClient_Success_TC1() 
    {
        String firstName = "John";
        String lastName = "Doe";
        String rawPassword = "Password123!";
        String encodedPassword = "encoded_password_abc";
        String email = "john.doe@example.com";

        AccountRegisterRequest request = new AccountRegisterRequest(email, firstName, lastName, rawPassword);

        org.mockito.Mockito.when(encoder.encode(rawPassword)).thenReturn(encodedPassword);

        clientService.registerClient(request);

        org.mockito.Mockito.verify(encoder).encode(rawPassword);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        org.mockito.Mockito.verify(repo).save(Objects.requireNonNull(clientCaptor.capture()));

        Client savedClient = clientCaptor.getValue();

        assertEquals(email, savedClient.getEmail());
        assertEquals(firstName, savedClient.getFirstName());
        assertEquals(encodedPassword, savedClient.getHashPassword()); 
    }

    @Test
    void testRegisterClientEmailAlreadyRegisteredTC2() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, "John", "Doe", "Password123!");
        org.mockito.Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(new Client(email, "John", "Doe", "encoded_password")));

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
    void testRegisterClientWeakPassword_TC4() 
    {
        String email = "john.doe@example.com";
        AccountRegisterRequest request = new AccountRegisterRequest(email, "John", "Doe", "weakpass");
        
        assertThrows(IllegalArgumentException.class, () -> 
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



    @Test  
    void testLogin_Success_TC1() 
    {
        String email = "john.doe@example.com";
        String password = "Password123!";
        String encodedPassword = "encoded_password_abc";

        Client client = new Client(email, "John", "Doe", encodedPassword);

        org.mockito.Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
        org.mockito.Mockito.when(encoder.matches(password, encodedPassword)).thenReturn(true);

        LoginRequest request = new LoginRequest(email, password);

        clientService.login(request);

        org.mockito.Mockito.verify(repo).findById(email);
        org.mockito.Mockito.verify(encoder).matches(password, encodedPassword);
    }

    @Test
    void testLoginInvalidPasswordTC2()
    {
        String email = "john.doe@example.com";
        String wrongPassword = "WrongPassword123!";
        String correctEncodedPassword = "encoded_password_abc";
        
        LoginRequest request = new LoginRequest(email, wrongPassword);
        
        Client client = new Client(email, "John", "Doe", correctEncodedPassword);

        org.mockito.Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
        org.mockito.Mockito.when(encoder.matches(wrongPassword, correctEncodedPassword)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
        {
            clientService.login(request);
        });

        org.mockito.Mockito.verify(repo).findById(email);
        org.mockito.Mockito.verify(encoder).matches(wrongPassword, correctEncodedPassword);
    }

    @Test
    void testLoginEmailNotFoundTC3()
    {
        String email = "nonexistent@example.com";
        String password = "Password123!";
        
        LoginRequest request = new LoginRequest(email, password);
        
        org.mockito.Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> 
        {
            clientService.login(request);
        });

        org.mockito.Mockito.verify(repo).findById(email);
    }


    @Test
    void testChangePassword_Success_TC1()
    {
        String email = "john.doe@example.com";
        String oldPassword = "OldPassword123!";
        String newPassword = "NewPassword123!";
        String encodedOldPassword = "encoded_old_password";
        String encodedNewPassword = "encoded_new_password";

        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        Client client = new Client(email, "John", "Doe", encodedOldPassword);

        org.mockito.Mockito.when(repo.findById(email)).thenReturn(java.util.Optional.of(client));
        org.mockito.Mockito.when(encoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);
        org.mockito.Mockito.when(encoder.encode(newPassword)).thenReturn(encodedNewPassword);

        clientService.changePassword(email, request);

        org.mockito.Mockito.verify(repo).findById(email);
        org.mockito.Mockito.verify(encoder).matches(oldPassword, encodedOldPassword);
        org.mockito.Mockito.verify(encoder).encode(newPassword);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        org.mockito.Mockito.verify(repo).save(java.util.Objects.requireNonNull(clientCaptor.capture()));

        Client savedClient = clientCaptor.getValue();
        
        assertEquals(encodedNewPassword, savedClient.getHashPassword());
    }
}
