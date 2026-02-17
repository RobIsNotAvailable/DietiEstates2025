package com.dd25.dietiestates25.service;

import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.dto.TokenLoginRequest;
import com.dd25.dietiestates25.model.Account;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.repository.AccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService
{
    private final AccountRepository repo;
    private final LoginTokenRepository tokenRepo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public AccountService(AccountRepository repo, LoginTokenRepository tokenRepo)
    {
        this.repo = repo;
        this.tokenRepo = tokenRepo;
    }

    public void login(LoginRequest request)
    {
        Account account = repo.findById(request.email()).orElseThrow(() -> 
            new SecurityException("Invalid credentials"));

        if (!encoder.matches(request.rawPassword(), account.getHashPassword()))
            throw new SecurityException("Invalid credentials");
    }

    @Transactional
    public void tokenLogin(@NonNull String tokenString, TokenLoginRequest request)
    {
        LoginToken token = tokenRepo.findById(tokenString).orElseThrow(() -> 
            new SecurityException("Invalid token"));

        if (!token.isValid())
            throw new SecurityException("Invalid or expired token");

        Account account = repo.findById(Objects.requireNonNull(token.getEmail())).orElseThrow(() -> 
            new IllegalStateException("Account not found"));

        account.setHashPassword(encoder.encode(request.newPassword()));

        token.setUsed(true);

        
    }
    
    @Transactional
    public void changePassword(@NonNull String requesterEmail, ChangePasswordRequest request)
    {
        Account account = repo.findById(requesterEmail).orElseThrow(() -> 
            new IllegalArgumentException("Account not found"));

        if (!encoder.matches(request.oldPassword(), account.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (request.oldPassword().equals(request.newPassword()))
            throw new IllegalStateException("New password must be different from the old password");

        account.setHashPassword(encoder.encode(request.newPassword()));
    }
}
