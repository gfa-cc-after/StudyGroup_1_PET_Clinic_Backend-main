package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.dtos.ErrorResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionErrorException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NameAlreadyBoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeletionErrorException.class)
    public ResponseEntity<ErrorResponse> handleDeletionError(DeletionErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Deletion Error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedActionException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid Credentials", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorResponse> handlePasswordException(PasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Password Error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NameAlreadyBoundException.class)
    public ResponseEntity<ErrorResponse> handleNameAlreadyBoundException(NameAlreadyBoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Name Already Bound", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Additional exception handlers can be added here

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
