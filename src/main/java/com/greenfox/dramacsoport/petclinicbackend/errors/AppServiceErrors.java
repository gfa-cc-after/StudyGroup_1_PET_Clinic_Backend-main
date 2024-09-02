package com.greenfox.dramacsoport.petclinicbackend.errors;

public class AppServiceErrors {

    //Validation errors
    public static final String EMAIL_FIELD_NOT_VALID = "Email field is not a valid email address";
    public static final String SHORT_PASSWORD = "Password must be longer than 3 characters.";

    //Authentication errors
    public static final String AUTHENTICATION_FAILED_BAD_CREDENTIALS = "Authentication failed! Bad credentials.";
    public static final String INCORRECT_PASSWORD = "Incorrect password!";

    //Database errors
    public static final String USERNAME_NOT_FOUND = "User cannot be found with this email: ";
    public static final String USERNAME_ALREADY_EXISTS = "Another user already exists by this email.";





}
