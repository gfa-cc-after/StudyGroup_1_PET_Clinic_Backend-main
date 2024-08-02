package com.greenfox.dramacsoport.petclinicbackend.errors;

import org.springframework.stereotype.Component;

@Component
public class AppServiceErrors {

    public static final String SHORT_PASSWORD_ERROR = "Password must be longer than 3 characters.";

    public String shortPasswordError() {
        return SHORT_PASSWORD_ERROR;
    }
}
