package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectLoginCredentialsException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;

import javax.naming.NameAlreadyBoundException;

public interface AuthService {

    AppUser registerUser(RegisterRequestDTO userRequest) throws InvalidPasswordException, NameAlreadyBoundException;
    /**
     * @param requestDTO The DTO, that is created when the user is try to log in. Contains the login credentials
     *                   (username & password).
     * @return A JWT token as a String for further authorization.
     * @throws IncorrectLoginCredentialsException when the username and/or the password is incorrect.
     */
    LoginResponseDTO login(LoginRequestDTO requestDTO) throws IncorrectLoginCredentialsException;

    boolean isPasswordLongerThanThreeChar(String password) throws InvalidPasswordException;
}
