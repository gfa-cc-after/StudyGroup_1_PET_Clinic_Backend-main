package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;

public interface AppUserService {
    DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException;

}
