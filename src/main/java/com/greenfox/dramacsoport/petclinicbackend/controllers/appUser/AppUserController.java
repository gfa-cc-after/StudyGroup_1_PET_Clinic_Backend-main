package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.controllers.appUser.auth.RegisterController;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class AppUserController {

    private final AppUserService appUserService;
    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(Principal user) {
        try {
            logger.info("Deleting user: {}", user.getName());
            return new ResponseEntity<>(appUserService.deleteUser(user.getName()), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error("Bad credentials", e);
            return new ResponseEntity<>("Bad credentials!", HttpStatus.FORBIDDEN);
        } catch (DeletionException e) {
            logger.error("Error while deleting user: {}", user.getName(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Error while deleting user", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
