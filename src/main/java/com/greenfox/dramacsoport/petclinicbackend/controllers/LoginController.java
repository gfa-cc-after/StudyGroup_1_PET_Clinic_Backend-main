package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.dtos.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserDetailService;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private MyUserService myUserService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailService myUserDetailService;

    @PostMapping("/login")
        public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO requestDTO) {
//        If user is not in the database, throws error.
            if (!myUserService.isUserRegistered(requestDTO.email())) {
                return ResponseEntity.badRequest().body("User is not registered");
            }

            if (myUserService.isPasswordMatching(requestDTO.email(), requestDTO.password())) {
                return ResponseEntity.ok().body(jwtService.generateToken(myUserDetailService.loadUserByUsername(requestDTO.email())));
            } else {
                //        If the password is incorrect.
                return ResponseEntity.badRequest().body("Bad credentials!");
            }
        }
}


