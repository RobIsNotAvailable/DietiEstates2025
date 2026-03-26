package com.dd25.dietiestates25.dto.request;

import org.springframework.lang.NonNull;
import com.dd25.dietiestates25.util.StringConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AgentStatsRequest(
    @NonNull
    @NotBlank(message = StringConstants.EMAIL_REQUIRED_MESSAGE)
    @Pattern
    (
        regexp = StringConstants.EMAIL_REGEX,
        message = StringConstants.INVALID_EMAIL_MESSAGE
    )
    String agentEmail,

    @Min(2020)
    int year,

    @Min(1)
    @Max(12)
    int month
) 
{ 
    public String agentEmail() 
    { 
        return agentEmail.trim().toLowerCase(); 
    }
}