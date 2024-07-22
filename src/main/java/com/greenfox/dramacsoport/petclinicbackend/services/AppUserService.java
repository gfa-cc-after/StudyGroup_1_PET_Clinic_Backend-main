package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.dtos.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserService {
    AppUser saveUser(AppUser user);

    AppUser registerUser(RegisterRequestDTO userRequest) throws RuntimeException;
    /**
     * @param requestDTO The DTO, that is created when the user is try to log in. Contains the login credentials
     *                   (username & password).
     * @return A JWT token as a String for further authorization.
     * @throws UsernameNotFoundException when the username and/or the password is incorrect.
     */
    String login(LoginRequestDTO requestDTO) throws UsernameNotFoundException;
}
