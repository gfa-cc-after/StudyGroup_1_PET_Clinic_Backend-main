package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.user.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.user.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.user.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.NameAlreadyBoundException;

public interface AppUserService {
    AppUser loadUserByEmail(String email) throws UsernameNotFoundException;

    DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException;

    EditUserResponseDTO changeUserData(String email, EditUserRequestDTO editUserRequest)
            throws IncorrectPasswordException, InvalidPasswordException,
            NameAlreadyBoundException;
}
