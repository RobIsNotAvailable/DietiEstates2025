package com.dd25.dietiestates25.util;

public final class ValidationConstants 
{
    private ValidationConstants() {}

    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$";
    public static final String PASSWORD_MESSAGE = "Password length must be between 8 and 32 characters long and contain both letters and numbers";
}