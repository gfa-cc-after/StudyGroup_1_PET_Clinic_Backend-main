package com.greenfox.dramacsoport.petclinicbackend.errors;

import org.springframework.stereotype.Component;

@Component
public class AppServiceErrors {

    public static final String SHORT_PASSWORD = "Password must be longer than 3 characters.";

    public static final String USER_ALREADY_EXISTS = "User already exists.";

    public static final String AUTHENTICATION_FAILED_BAD_CREDENTIALS = "Authentication failed! Bad credentials.";

    public static final String USERNAME_NOT_FOUND = "User cannot be found with this email: ";

    public String shortPassword() {
        return SHORT_PASSWORD;
    }

    public String userAlreadyExists() {
        return USER_ALREADY_EXISTS;
    }

    public String notFound() {
        return AUTHENTICATION_FAILED_BAD_CREDENTIALS;
    }

    public String usernameNotFound(String email) {
        return USERNAME_NOT_FOUND;
    }
}
