package com.greenfox.dramacsoport.petclinicbackend.exceptions;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;

public class IncorrectLoginCredentialsException extends RuntimeException {
    public IncorrectLoginCredentialsException() {
        super(AppServiceErrors.AUTHENTICATION_FAILED_BAD_CREDENTIALS);
    }
}
