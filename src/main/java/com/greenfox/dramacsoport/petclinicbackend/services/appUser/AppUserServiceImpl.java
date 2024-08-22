package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository appUserRepository;
    private final AppServiceErrors error;

    @Override
    public DeleteUserResponse deleteUser(String userEmail) throws DeletionException {
        AppUser userToDelete = appUserRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(error.usernameNotFound(userEmail)));

        if (userToDelete.getPets().isEmpty()) {
            appUserRepository.delete(userToDelete);
            return new DeleteUserResponse("Your profile has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete your profile. Please transfer or delete your pets before proceeding.");
        }
    }
}
