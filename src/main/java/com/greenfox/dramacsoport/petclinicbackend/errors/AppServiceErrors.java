package com.greenfox.dramacsoport.petclinicbackend.errors;

import org.springframework.stereotype.Component;

@Component
public class AppServiceErrors {

    public static final String SHORT_PASSWORD = "Password must be longer than 3 characters.";

    public static final String USER_ALREADY_EXISTS = "User already exists.";

    public static final String NOT_FOUND = "Authentication failed! User not found.";

    public static final String USERNAME_NOT_FOUND = "user cannot be found with this email: ";

    public String shortPassword() {
        return SHORT_PASSWORD;
    }

    public String userAlreadyExists() {
        return USER_ALREADY_EXISTS;
    }

    public String notFound() {
        return NOT_FOUND;
    }

    public String usernameNotFound(String email) {
        return USERNAME_NOT_FOUND;
    }
}
