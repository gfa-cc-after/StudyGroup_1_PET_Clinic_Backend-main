package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;

import javax.naming.NameAlreadyBoundException;

public interface AppUserService {
    DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException;

    EditUserResponseDTO changeUserData(AppUser user, EditUserRequestDTO editUserRequest) throws PasswordException,
            NameAlreadyBoundException;
}
