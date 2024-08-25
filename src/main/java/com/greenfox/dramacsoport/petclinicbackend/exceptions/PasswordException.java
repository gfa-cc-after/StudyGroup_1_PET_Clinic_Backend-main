package com.greenfox.dramacsoport.petclinicbackend.exceptions;

public class PasswordException extends RuntimeException {

    // Constructor that allows a custom message
    public PasswordException(String message) {
        super(message);
    }
}
