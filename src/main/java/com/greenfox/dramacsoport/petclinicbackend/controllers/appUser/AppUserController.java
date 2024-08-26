package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;
    //private final Auth
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal user) throws DeletionException {
        String userEmail = user.getName();

        logger.info("Deleting user: {}", userEmail);
        return new ResponseEntity<>(appUserService.deleteUser(userEmail, id), HttpStatus.OK);
    }

//    @PostMapping({"/user/profile", "admin/profile"})
//    public ResponseEntity<?> editUserData(Principal principal, @RequestBody EditUserRequestDTO editUserRequest) {
//        AppUser user = appUserService.loadUserByUsername(principal.getName());
//        try {
//            return new ResponseEntity<>(appUserService.changeUserData(user, editUserRequest), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
}
