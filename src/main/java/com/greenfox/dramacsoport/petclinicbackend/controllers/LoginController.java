package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:8082")
@RestController
public class LoginController {

        @Autowired
        private MyUserService myUserService;

        @PostMapping("/login")
        @ResponseBody
        public ResponseEntity<?> loginUser(@RequestBody MyUser user) {

//        If the user is already created redirect to home page.

//        If the password is only 3 characters long show an error message.
            if (!myUserService.isPasswordLongerThanThreeChar(user.getPassword())){
                return ResponseEntity.badRequest().body("Password must be longer than 3 characters.");
            }

//        If even one field is not filled then show an error message.
            if (myUserService.isMissingLoginCredential(user)){
                return ResponseEntity.badRequest().body("All fields are required.");
            }
// If the user is registered, it redirects to home page.
            if (myUserService.isUserRegistered(user.getEmail())) {
                MyUser registeredUser = myUserService.returnUser(user.getEmail());

                // Check user role and redirect accordingly
                if (registeredUser.getRole().equals(Role.ADMIN)) {
                    return ResponseEntity.ok().body("Redirect to: /admin/home");
                } else if (registeredUser.getRole().equals(Role.USER)) {
                    return ResponseEntity.ok().body("Redirect to: /user/home");
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


