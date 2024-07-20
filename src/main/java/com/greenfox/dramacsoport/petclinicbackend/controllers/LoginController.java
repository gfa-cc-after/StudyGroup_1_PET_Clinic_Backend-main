package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO requestDTO) {
        ResponseEntity<String> response;
        try {
            response = ResponseEntity.ok(appUserService.login(requestDTO));
        } catch (UsernameNotFoundException e) {
            response = new ResponseEntity<>("Bad credentials!", HttpStatus.UNAUTHORIZED);
        }
        return response;
    }
}