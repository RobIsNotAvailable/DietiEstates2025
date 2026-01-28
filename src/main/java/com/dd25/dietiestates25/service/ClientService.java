package com.dd25.dietiestates25.service;

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
        if (repo.findById(email).isPresent())
            throw new IllegalStateException("Email already registered");

        validatePassword(rawPassword);

        Client client = new Client(email, firstName, lastName, encoder.encode(rawPassword));

        repo.save(client);
    }

    public void login(@NonNull String email, String rawPassword)
    {
        Client client = repo.findById(email).orElseThrow(() -> 
            new SecurityException("Invalid credentials"));
        
        if (!encoder.matches(rawPassword, client.getHashPassword()))
            throw new SecurityException("Invalid credentials");
    }

    @Transactional
    public void changePassword(@NonNull String requesterEmail, String oldRawPassword, String newRawPassword)
    {
        Client requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new SecurityException("Account not found"));
        
        if (!encoder.matches(oldRawPassword, requester.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (oldRawPassword.equals(newRawPassword))
            throw new IllegalStateException("New password must be different from the old password");

        validatePassword(newRawPassword);

        requester.setHashPassword(encoder.encode(newRawPassword));
    }

    private void validatePassword(String password)
    {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers");
    }
}
