package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final PetService petService;
    private final AppUserService appUserService;

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/user/home")
    public ResponseEntity<?> handleUserHome(Principal user) {
        return new ResponseEntity<>(petService.getUserPets(user.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(Principal user) {
        System.out.println(user.getName());
        try {
            return new ResponseEntity<>(appUserService.deleteUser(user.getName()), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("Bad credentials!", HttpStatus.FORBIDDEN);
        }
    }
}
