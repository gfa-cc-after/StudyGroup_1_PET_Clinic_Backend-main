package com.greenfox.dramacsoport.petclinicbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DeletionErrorException extends Exception {
    public DeletionErrorException(String message) {
        super(message);
    }
}
