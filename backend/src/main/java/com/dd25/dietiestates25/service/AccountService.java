package com.dd25.dietiestates25.service;

import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dd25.dietiestates25.dto.AuthResponse;
import com.dd25.dietiestates25.dto.ChangePasswordRequest;
import com.dd25.dietiestates25.dto.LoginRequest;
import com.dd25.dietiestates25.dto.ResetPasswordRequest;
import com.dd25.dietiestates25.dto.TokenLoginRequest;
import com.dd25.dietiestates25.model.Account;
import com.dd25.dietiestates25.model.LoginToken;
import com.dd25.dietiestates25.repository.AccountRepository;
import com.dd25.dietiestates25.repository.LoginTokenRepository;
import com.dd25.dietiestates25.service.utilityservice.EmailService;
import com.dd25.dietiestates25.service.utilityservice.JwtService;
import com.dd25.dietiestates25.util.SecurityUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AccountService
{
    private final AccountRepository repo;
    private final LoginTokenRepository tokenRepo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final SecurityUtil securityUtil;

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";


    public AuthResponse login(LoginRequest request)
    {
        Account account = repo.findById(request.email()).orElseThrow(() -> 
            new SecurityException(INVALID_CREDENTIALS_MESSAGE));

        if (!encoder.matches(request.rawPassword(), account.getHashPassword()))
            throw new SecurityException(INVALID_CREDENTIALS_MESSAGE);

        String token = jwtService.generateToken(account);
        return new AuthResponse(token);
    }

    @Transactional
    public AuthResponse tokenLogin(@NonNull String tokenString, TokenLoginRequest request)
    {
        LoginToken loginToken = tokenRepo.findById(tokenString).orElseThrow(() -> 
            new SecurityException(INVALID_TOKEN_MESSAGE));

        if (!loginToken.isValid())
            throw new SecurityException(INVALID_TOKEN_MESSAGE);

        Account account = repo.findById(Objects.requireNonNull(loginToken.getEmail())).orElseThrow(() -> 
            new IllegalArgumentException(ACCOUNT_NOT_FOUND_MESSAGE));

        account.setHashPassword(encoder.encode(request.newPassword()));

        loginToken.setUsed(true);

        String jwt = jwtService.generateToken(account);
        return new AuthResponse(jwt);   
    }
    
    @Transactional
    public void changePassword(ChangePasswordRequest request)
    {
        Account account = repo.findById(securityUtil.getCurrentEmail()).orElseThrow(() -> 
            new IllegalArgumentException(ACCOUNT_NOT_FOUND_MESSAGE));

        if (!encoder.matches(request.oldPassword(), account.getHashPassword()))
            throw new SecurityException("Old password is incorrect");

        if (request.oldPassword().equals(request.newPassword()))
            throw new IllegalStateException("New password must be different from the old password");

        account.setHashPassword(encoder.encode(request.newPassword()));
    }

    public void forgotPassword(ResetPasswordRequest request)
    {
        String email = request.email();
        
        repo.findById(email).orElseThrow(() -> 
            new IllegalArgumentException(ACCOUNT_NOT_FOUND_MESSAGE));
        
        LoginToken token = new LoginToken(email);
        tokenRepo.save(token);
        emailService.sendPasswordResetEmail(email, token.getToken());
    }
}
