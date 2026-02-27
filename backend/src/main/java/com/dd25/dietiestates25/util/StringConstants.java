package com.dd25.dietiestates25.util;

public final class StringConstants 
{
    private StringConstants() {}

    /* DTO validators */
    public static final String INVALID_EMAIL_MESSAGE = "Invalid email format";
    public static final String EMAIL_REQUIRED_MESSAGE = "Email field is required";
    public static final String FIRST_NAME_REQUIRED_MESSAGE = "First name field is required";
    public static final String LAST_NAME_REQUIRED_MESSAGE = "Last name field is required";

    public static final String PASSWORD_REQUIRED_MESSAGE = "Password field is required";
    public static final String OLD_PASSWORD_REQUIRED_MESSAGE = "Old password field is required";
    public static final String NEW_PASSWORD_REQUIRED_MESSAGE = "New password field is required";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{8,32}$";
    public static final String PASSWORD_MESSAGE = "Password must be between 8 and 32 characters long and must contain both letters and numbers";

    /* Service errors */
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String EMAIL_ALREADY_REGISTERED_MESSAGE = "Email already registered";
    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
}