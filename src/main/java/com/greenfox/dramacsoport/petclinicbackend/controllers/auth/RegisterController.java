package com.greenfox.dramacsoport.petclinicbackend.controllers.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AppUserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private AppUserAuthService appUserAuthService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO newUserDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            appUserAuthService.registerUser(newUserDTO);
            return new ResponseEntity<>("User registered", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

