package com.dd25.dietiestates25.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.request.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.response.AuthResponse;
import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.AccountRepository;
import com.dd25.dietiestates25.service.utilityservice.JwtService;
import com.dd25.dietiestates25.util.StringConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ClientService 
{
    private final AccountRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthResponse registerClient(AccountRegisterRequest request)
    {
        repo.findById(request.email()).ifPresent(client -> 
            {throw new IllegalStateException(StringConstants.EMAIL_ALREADY_REGISTERED_MESSAGE);});

        Client client = new Client(request.email(), request.firstName(), request.lastName(), encoder.encode(request.rawPassword()));
        repo.save(client);  

        String token = jwtService.generateToken(client);
        return new AuthResponse(token);
    }
}
