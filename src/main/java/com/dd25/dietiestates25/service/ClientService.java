package com.dd25.dietiestates25.service;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.model.Client;
import com.dd25.dietiestates25.repository.ClientRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientService 
{
    private final ClientRepository repo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ClientService(ClientRepository repo)
    {
        this.repo = repo;
    }

    public void registerClient(@NonNull String email, String firstName, String lastName, String rawPassword)
    {
        if (!rawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
        {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers");
        }

        Client client = new Client(email, firstName, lastName, encoder.encode(rawPassword));

        if (repo.findById(email).isPresent())
        {
            throw new IllegalStateException("Email already registered");
        }

        repo.save(client);
    }

    public void login(@NonNull String email, String rawPassword)
    {
        Optional<Client> clientOptional = repo.findById(email);

        if (clientOptional.isPresent())
        {
            Client client = clientOptional.get();
            if (!encoder.matches(rawPassword, client.getPasswordHash()))
            {
                throw new SecurityException("Invalid credentials");
            }
        }
        else
        {
            throw new SecurityException("Invalid credentials");
        }
    }

    @Transactional
    public void changePassword(@NonNull String email, String oldRawPassword, String newRawPassword)
    {
        Optional<Client> clientOptional = repo.findById(email);

        if (clientOptional.isPresent())
        {
            Client client = clientOptional.get();
            if (!encoder.matches(oldRawPassword, client.getPasswordHash()))
            {
                throw new SecurityException("Old password is incorrect");
            }

            if (!newRawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            {
                throw new IllegalArgumentException("New password must be at least 8 characters long and contain both letters and numbers");
            }

            if (encoder.matches(newRawPassword, client.getPasswordHash()))
            {
                throw new IllegalArgumentException("New password must be different from the old password");
            }

            client.setPasswordHash(encoder.encode(newRawPassword));
        }
        else
        {
            throw new SecurityException("Client not found");
        }
    }
}
