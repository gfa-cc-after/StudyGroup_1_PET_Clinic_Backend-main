package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.DeletionException;

public interface AppUserService {
    DeleteUserResponse deleteUser(String userEmail) throws DeletionException;

}
