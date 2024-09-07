package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;

import javax.naming.NameAlreadyBoundException;

public interface AppUserService {
    DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException;

    EditUserResponseDTO changeUserData(String email, EditUserRequestDTO editUserRequest) throws IncorrectPasswordException, InvalidPasswordException,
            NameAlreadyBoundException;
}
