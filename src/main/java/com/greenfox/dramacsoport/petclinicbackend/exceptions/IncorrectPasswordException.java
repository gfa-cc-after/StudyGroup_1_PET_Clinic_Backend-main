package com.greenfox.dramacsoport.petclinicbackend.exceptions;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;

public class IncorrectPasswordException extends RuntimeException {

    // Constructor that allows a custom message
    public IncorrectPasswordException() {
        super(AppServiceErrors.INCORRECT_PASSWORD);
    }
}
