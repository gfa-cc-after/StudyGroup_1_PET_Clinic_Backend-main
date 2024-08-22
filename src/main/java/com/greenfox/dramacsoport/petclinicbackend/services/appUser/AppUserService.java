package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.NameAlreadyBoundException;

public interface AppUserService {
    AppUser saveUser(AppUser user);

    /**
     * <h3>Looks for an entity in the storage and gives back a UserDetails object made from it.</h3>
     *
     * @param email the email, that is used as a username
     * @return an implementation of the security core UserDetails interface, NOT the same as the AppUser Entity
     * @throws UsernameNotFoundException when no entity found under this email.
     */
    AppUser loadUserByUsername(String email) throws UsernameNotFoundException;

    AppUser registerUser(RegisterRequestDTO userRequest) throws PasswordException, NameAlreadyBoundException;
    /**
     * @param requestDTO The DTO, that is created when the user is try to log in. Contains the login credentials
     *                   (username & password).
     * @return A JWT token as a String for further authorization.
     * @throws UsernameNotFoundException when the username and/or the password is incorrect.
     */
    LoginResponseDTO login(LoginRequestDTO requestDTO) throws UsernameNotFoundException;

}
