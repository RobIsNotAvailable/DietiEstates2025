package com.dd25.dietiestates25.service;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.AccountRegisterRequest;
import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
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

    public void registerClient(@NonNull String email, AccountRegisterRequest request)
    {
        if (repo.findById(email).isPresent())
            throw new IllegalStateException("Email already registered");

        validatePassword(request.rawPassword());

        Client client = new Client(email, request.firstName(), request.lastName(), encoder.encode(request.rawPassword()));

        repo.save(client);
    }

    public void login(LoginRequest request)
    {
        Client client = repo.findById(request.email()).orElseThrow(() -> 
            new SecurityException("Invalid credentials"));
        
        if (!encoder.matches(request.rawPassword(), client.getHashPassword()))
            throw new SecurityException("Invalid credentials");
    }

    @Transactional
    public void changePassword(@NonNull String requesterEmail, ChangePasswordRequest request)
    {
        Client requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new SecurityException("Account not found"));
        
        if (!encoder.matches(request.oldPassword(), requester.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (request.oldPassword().equals(request.newPassword()))
            throw new IllegalStateException("New password must be different from the old password");

        validatePassword(request.newPassword());

        requester.setHashPassword(encoder.encode(request.newPassword()));
    }

    private void validatePassword(String password)
    {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers");
    }
}
