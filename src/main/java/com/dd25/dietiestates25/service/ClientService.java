package com.dd25.dietiestates25.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.ClientRepository;

@Service
public class ClientService 
{
    private final ClientRepository repo;

    private final PasswordEncoder encoder;

    public ClientService(ClientRepository repo, PasswordEncoder encoder)
    {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void registerClient(AccountRegisterRequest request)
    {
        repo.findById(request.email()).ifPresent(client -> 
            {throw new IllegalStateException("Email already registered");});

        Client client = new Client(request.email(), request.firstName(), request.lastName(), encoder.encode(request.rawPassword()));
        repo.save(client);  
    }
}
