package com.greenfox.dramacsoport.petclinicbackend.controllers.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AppUserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AppUserAuthService appUserAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO requestDTO,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(appUserAuthService.login(requestDTO), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Bad credentials!", HttpStatus.FORBIDDEN);
        }
    }

}