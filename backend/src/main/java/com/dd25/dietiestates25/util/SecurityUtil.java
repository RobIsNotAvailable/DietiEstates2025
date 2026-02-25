package com.dd25.dietiestates25.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil
{
    public String getCurrentEmail()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
