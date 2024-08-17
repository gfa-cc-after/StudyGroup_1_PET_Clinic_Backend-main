package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.PetListResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Controller
public class MainController {

    private final PetService petService;
    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/user/home")
    public ResponseEntity<?> handleUserHome(HttpServletRequest request){

        return new ResponseEntity<>(petService.getUserPets(request), HttpStatus.OK);
    }


}
