package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Principal user) {
        String userEmail = user.getName();

        logger.info("Deleting user: {}", user.getName());
        return new ResponseEntity<>(appUserService.deleteUser(userEmail, id), HttpStatus.OK);
    }
}
