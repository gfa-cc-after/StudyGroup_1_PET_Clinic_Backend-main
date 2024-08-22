package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.exeptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(Principal user) {
        try {
            return new ResponseEntity<>(appUserService.deleteUser(user.getName()), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Bad credentials!", HttpStatus.FORBIDDEN);
        } catch (DeletionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
