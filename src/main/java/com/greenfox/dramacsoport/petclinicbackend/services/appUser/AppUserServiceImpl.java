package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = new ModelMapper();

    private final JwtService jwtService;

    private final AuthService authService;

    @Override
    public DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException {
        AppUser userToDelete = appUserRepository.findByEmail(userEmail);

        if (!userToDelete.getId().equals(id)) {
            throw new UnauthorizedActionException("User is not authorized to delete this account");
        } else if (userToDelete.getPets().isEmpty()) {
            appUserRepository.delete(userToDelete);
            return new DeleteUserResponse("Your profile has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete your profile. " +
                    "Please transfer or delete your pets before proceeding.");
        }
    }

    @Override
    public EditUserResponseDTO changeUserData(String email, EditUserRequestDTO request) throws IncorrectPasswordException,
            NameAlreadyBoundException {

        AppUser user = appUserRepository.findByEmail(email);
        String newPassword = request.password();


        //check if new email is not already taken - NameAlreadyBoundException
        if (appUserRepository.existsByEmail(request.email()) && !request.email().equals(user.getEmail())) {
            throw new NameAlreadyBoundException(AppServiceErrors.USERNAME_ALREADY_EXISTS);
        }
        //check if old pw is valid - PWException
        if (!passwordEncoder.matches(request.originalPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (request.password() == null) {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            newPassword = user.getPassword();
        } else {
            authService.isPasswordLongerThanThreeChar(request.password());

            //check if new pw is not the same as old pw - PWException
            if (passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new InvalidPasswordException("New password cannot be the same as the old one.");
            }
        }

        modelMapper.map(request, user);

        user.setPassword(passwordEncoder.encode(newPassword));

        //save user
        appUserRepository.save(user);

        //if password has been changed, log out user
        logoutIfPasswordHasChanged(request.password(), request.originalPassword());

        return new EditUserResponseDTO("Changes saved.");
    }

    private void logoutIfPasswordHasChanged(String newPassword, String oldPassword) {
        if (!newPassword.equals(oldPassword)) {
            jwtService.logoutUser();
            System.out.println("Logged out due to password change.");
        }
    }

}
