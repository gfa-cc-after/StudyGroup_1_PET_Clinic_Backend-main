package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
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
    public DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException {
        AppUser userToDelete = appUserRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(error.usernameNotFound(userEmail)));

        if (!userToDelete.getId().equals(id)) {
            throw new UnauthorizedActionException("User is not authorized to delete this account");
        } else if (userToDelete.getPets().isEmpty()) {
            appUserRepository.delete(userToDelete);
            return new DeleteUserResponse("Your profile has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete your profile. Please transfer or delete your pets before proceeding.");
        }
    }
}
