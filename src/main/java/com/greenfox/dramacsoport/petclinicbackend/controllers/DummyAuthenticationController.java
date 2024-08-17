package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DummyAuthenticationController {
    // TODO - after proper test cases this Controller can be deleted

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private JwtService jwtService;

    @GetMapping("/home")
    public String handleWelcome() {
        return "index";
    }


}
