package com.dd25.dietiestates25.dto;

import org.springframework.lang.NonNull;

public record AuthResponse(
    @NonNull
    String token
) {}