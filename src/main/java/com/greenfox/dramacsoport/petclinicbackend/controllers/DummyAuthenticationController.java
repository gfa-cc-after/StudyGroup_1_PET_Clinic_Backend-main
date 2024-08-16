package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.AppUserDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/user/home")
    public ResponseEntity<?> handleUserHome(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Claims claims= jwtService.getClaims(jwtService.stripBearer(token));

        String email = claims.getSubject();
        Optional<AppUser> user = Optional.ofNullable(appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        AppUser appUser = user.get();

        modelMapper.typeMap(Pet.class, PetDTO.class);
        AppUserDTO appUserDTO = modelMapper.map(appUser, AppUserDTO.class);

        appUserDTO.setPets(appUser.getPets().stream()
                .map(pet -> modelMapper.map(pet, PetDTO.class))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
    }
}
