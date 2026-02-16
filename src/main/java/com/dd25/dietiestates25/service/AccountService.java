package com.dd25.dietiestates25.service;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.model.Account;
import com.dd25.dietiestates25.repository.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService
{
    private final AccountRepository repo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public AccountService(AccountRepository repo)
    {
        this.repo = repo;
    }

    public void login(LoginRequest request)
    {
        Account account = repo.findById(request.email()).orElseThrow(() -> 
            new SecurityException("Invalid credentials"));
        
        if (!encoder.matches(request.rawPassword(), account.getHashPassword()))
            throw new SecurityException("Invalid credentials");
    }

    
    @Transactional
    public void changePassword(@NonNull String requesterEmail, ChangePasswordRequest request)
    {
        Account requester = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));

        if (!encoder.matches(request.oldPassword(), requester.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (request.oldPassword().equals(request.newPassword()))
            throw new IllegalStateException("New password must be different from the old password");

        requester.setHashPassword(encoder.encode(request.newPassword()));
    }
}
