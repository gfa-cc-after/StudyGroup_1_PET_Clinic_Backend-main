package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin("http://localhost:8082")
@RestController
public class LoginController {

        @Autowired
        private MyUserService myUserService;

        @PostMapping("/login")
        @ResponseBody
        public ResponseEntity<?> loginUser(@RequestBody MyUser user) {

//        If even one field is not filled then show an error message.
            if (myUserService.isMissingLoginCredential(user)){
                return ResponseEntity.badRequest().body("All fields are required.");
            }

//        If the password is only 3 characters long show an error message.
            if (!myUserService.isPasswordLongerThanThreeChar(user.getPassword())){
                return ResponseEntity.badRequest().body("Password must be longer than 3 characters.");
            }

//        If the user is registered, it redirects to home page.
            if (myUserService.isUserRegistered(user.getEmail())) {
                MyUser registeredUser = myUserService.returnUser(user.getEmail());

                // Check user role and redirect accordingly
                if (registeredUser.getRole().equals(Role.ADMIN)) {
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("/admin/home"))
                            .build();
                } else if (registeredUser.getRole().equals(Role.USER)) {
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create("/user/home"))
                            .build();
                } else {
                    return ResponseEntity.badRequest().body("User role is not recognized.");
                }
            }
//        If user not in database, throws error.
            else {
                    return ResponseEntity
                            .badRequest()
                            .body("User is not registered");
            }
        }
    }


