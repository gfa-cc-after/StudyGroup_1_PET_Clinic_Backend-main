package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
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
    private final AppUserRepository userRepo;
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal user) throws DeletionException {
        String userEmail = user.getName();

        logger.info("Deleting user: {}", userEmail);
        return new ResponseEntity<>(appUserService.deleteUser(userEmail, id), HttpStatus.OK);
    }

    @PostMapping({"/user/profile", "admin/profile"})
    public ResponseEntity<?> editUserData(Principal principal, @RequestBody EditUserRequestDTO editUserRequest) throws NameAlreadyBoundException {
        AppUser user = userRepo.findByEmail(principal.getName());
        return new ResponseEntity<EditUserResponseDTO>(appUserService.changeUserData(user, editUserRequest),
                HttpStatus.OK);
    }
}
