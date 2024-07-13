package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:8082")
@RestController
public class RegisterController {

    @Autowired
    private MyUserService myUserService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody MyUser user) {

//        If the user is already created show an error message.
        if (myUserService.isUserRegistered(user.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists.");
        }
//        If the password is only 3 characters long show an error message.
        if (!myUserService.isPasswordLongerThanThreeChar(user.getPassword())){
            return ResponseEntity.badRequest().body("Password must be longer than 3 characters.");
        }

//        If even one field is not filled then show an error message.
        if (myUserService.isMissingRegisterCredential(user)){
            return ResponseEntity.badRequest().body("All fields are required.");
        }
//        If no user is stored with that data store it in the database.
        myUserService.saveUser(user);  //DTO
        return ResponseEntity.ok().body("User registered");
    }
}
