package com.dd25.dietiestates25.dto.request;

import org.springframework.lang.NonNull;

import com.dd25.dietiestates25.util.StringConstants;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NonNull
    @NotBlank (message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    String email,

    @NotBlank (message = StringConstants.PASSWORD_REQUIRED_MESSAGE)
    String rawPassword
) 
{ public String email() { return email.trim().toLowerCase(); }}