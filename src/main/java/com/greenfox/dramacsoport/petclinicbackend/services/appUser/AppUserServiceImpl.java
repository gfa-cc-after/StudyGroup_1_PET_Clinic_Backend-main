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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Override
    public AppUser loadUserByEmail(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND + email));
    }

    @Override
    public AppUser loadUserByEmail(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND + email));
    }

    @Override
    public DeleteUserResponse deleteUser(String userEmail, Long id) throws DeletionException {
        AppUser userToDelete = loadUserByEmail(userEmail);

        if (!userToDelete.getId().equals(id)) {
            throw new UnauthorizedActionException("User is not authorized to delete this account");
        } else if (userToDelete.getPets().isEmpty()) {
            appUserRepository.delete(userToDelete);
            logger.info("User deleted with email: {}", userToDelete.getEmail());
            return new DeleteUserResponse("Your profile has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete your profile. Please transfer or delete your pets before proceeding.");
        }
    }

    @Override
    public EditUserResponseDTO changeUserData(String email, EditUserRequestDTO request) throws IncorrectPasswordException,
            NameAlreadyBoundException {

        modelMapper.typeMap(EditUserRequestDTO.class, AppUser.class)
                .addMappings(mapping -> {
                    mapping.skip(EditUserRequestDTO::password,
                            (destination, value) -> destination.setPassword((String) value));
                });

        AppUser user = loadUserByEmail(email);
        final String oldEncodedPassword = user.getPassword();
        logger.debug("encoded PW from db: %s".formatted(oldEncodedPassword));
        String newEncodedPassword;

        //check if new email is not already taken - NameAlreadyBoundException
        if (appUserRepository.existsByEmail(request.email()) && !request.email().equals(user.getEmail())) {
            throw new NameAlreadyBoundException(AppServiceErrors.USERNAME_ALREADY_EXISTS);
        }
        //check if old pw is valid - PWException
        if (!passwordEncoder.matches(request.originalPassword(), oldEncodedPassword)) {
            throw new IncorrectPasswordException();
        }

        if (request.password() == null) {
            newEncodedPassword = oldEncodedPassword;
            logger.info("Password field is null, so new password will be the same as the encoded old password");
            logger.debug("encoded PW old and new: %s".formatted(newEncodedPassword));
        } else {

            //check if new pw is not the same as old pw - PWException
            if (passwordEncoder.matches(request.password(), oldEncodedPassword)) {
                throw new InvalidPasswordException("New password cannot be the same as the old one.");
            }
            logger.info("Password field is NOT null, so new password will be encoded right now.");
            newEncodedPassword = passwordEncoder.encode(request.getPassword());
            logger.debug("encoded new PW upon creation: %s".formatted(newEncodedPassword));
        }

        user.setPassword(newEncodedPassword);
        logger.info("New password set.");

        modelMapper.map(request, user);

        //save user
        appUserRepository.save(user);
        logger.info("AppUser entity mapped and updated.");
        logger.debug("encoded PW after mapping and saving in db: %s".formatted(user.getPassword()));

        //if password has been changed, log out user
        logoutIfPasswordHasChanged(newEncodedPassword, oldEncodedPassword);

        return new EditUserResponseDTO("New user data saved.");
    }

    private void logoutIfPasswordHasChanged(String newPassword, String oldPassword) {
        if (!newPassword.equals(oldPassword)) {
            jwtService.logoutUser();
            logger.info("Logged out due to password change.");
        }
    }

}
