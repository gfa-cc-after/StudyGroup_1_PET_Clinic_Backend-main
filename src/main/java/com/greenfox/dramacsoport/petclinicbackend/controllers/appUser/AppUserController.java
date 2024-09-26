package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.user.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.user.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NameAlreadyBoundException;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal user) throws DeletionException {
        String userEmail = user.getName();

        logger.info("Deleting user: {}", userEmail);
        return new ResponseEntity<>(appUserService.deleteUser(userEmail, id), HttpStatus.OK);
    }

    @PatchMapping({"/profile"})
    public ResponseEntity<EditUserResponseDTO> editUserData(Principal principal,
                                          @Valid @RequestBody EditUserRequestDTO editUserRequest) throws NameAlreadyBoundException {
        return new ResponseEntity<>(appUserService.changeUserData(principal.getName(), editUserRequest), HttpStatus.OK);
    }
}
