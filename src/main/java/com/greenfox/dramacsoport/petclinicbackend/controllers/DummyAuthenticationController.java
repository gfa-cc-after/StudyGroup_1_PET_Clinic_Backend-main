package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin("http://localhost:8082")
public class DummyAuthenticationController {
    // TODO - after proper test cases this Controller can be deleted

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String handleWelcome() {
        return "index";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/user/home")
    public String handleUserHome() {
        return "home_user";
    }

//    @PostMapping("/register")
//    @ResponseBody
//    public MyUser createUser(@RequestBody MyUser user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return myUserRepository.save(user);
//    }
}
