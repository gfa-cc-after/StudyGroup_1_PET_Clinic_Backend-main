package com.greenfox.dramacsoport.petclinicbackend.errors;

import org.springframework.stereotype.Component;

@Component
public class AppServiceErrors {

    public static final String SHORT_PASSWORD = "Password must be longer than 3 characters.";

    public static final String USER_ALREADY_EXISTS = "User already exists.";

    public String shortPassword() {
        return SHORT_PASSWORD;
    }

    public String userAlreadyExists() {
        return USER_ALREADY_EXISTS;
    }
}
