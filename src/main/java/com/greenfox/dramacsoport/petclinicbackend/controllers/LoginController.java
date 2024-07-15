package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
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
        public ResponseEntity<?> loginUser(@RequestBody MyUser user) {
//        If user is not in the database, throws error.
            if (!myUserService.isUserRegistered(user.getEmail())) {
                return ResponseEntity.badRequest().body("User is not registered");
            }

            if (myUserService.isPasswordMatching(user.getEmail(), user.getPassword())) {
                return ResponseEntity.ok().body(jwtService.generateToken(myUserDetailService.loadUserByUsername(user.getEmail())));
            } else {
                //        If the password is incorrect.
                return ResponseEntity.badRequest().body("Bad credentials!");
            }
        }
}


