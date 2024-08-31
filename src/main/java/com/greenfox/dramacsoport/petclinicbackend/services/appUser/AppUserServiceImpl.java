package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

    private final JwtService jwtService;

    @Override
    public DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException {
        AppUser userToDelete = appUserRepository.findByEmail(userEmail);

        if (!userToDelete.getId().equals(id)) {
            throw new UnauthorizedActionException("User is not authorized to delete this account");
        } else if (userToDelete.getPets().isEmpty()) {
            appUserRepository.delete(userToDelete);
            return new DeleteUserResponse("Your profile has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete your profile. Please transfer or delete your pets before proceeding.");
        }
    }

    @Override
    public EditUserResponseDTO changeUserData(String email, EditUserRequestDTO request) throws PasswordException,
            NameAlreadyBoundException {

        AppUser user = appUserRepository.findByEmail(email);

        //check if new email is not already taken - NameAlreadyBoundException
        if (appUserRepository.existsByEmail(request.email()) && !request.email().equals(user.getEmail())) {
            throw new NameAlreadyBoundException(AppServiceErrors.USERNAME_ALREADY_EXISTS);
        }
        //check if old pw is valid - PWException
        if (!passwordEncoder.matches(request.prevPassword(), user.getPassword())) {
            throw new PasswordException("Incorrect password. Please try again!");
        }
        //check if new pw is not the same as old pw - PWException
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new PasswordException("New password cannot be the same as the old one.");
        }

        //map the request to the user entity
        AppUser updatedUser = modelMapper.map(request, AppUser.class);
        updatedUser.setPassword(passwordEncoder.encode(request.newPassword()));

        //save user
        appUserRepository.save(updatedUser);

        //if password has been changed, log out user
        logoutIfPasswordHasChanged(request.newPassword(), request.prevPassword());

        //return OK message for now - TODO: discuss the return
        return new EditUserResponseDTO("New user data saved.");
    }

    private void logoutIfPasswordHasChanged(String newPassword, String oldPassword) {
        if (!newPassword.equals(oldPassword)) {
            jwtService.logoutUser();
            System.out.println("Logged out due to password change.");
        }
    }

}
