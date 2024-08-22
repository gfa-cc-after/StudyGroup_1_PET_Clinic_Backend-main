package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class PetController {

    private final PetService petService;
    private final Logger logger = LoggerFactory.getLogger(PetController.class);

    @GetMapping("/user/pets")
    public ResponseEntity<?> getPets(Principal user) {
        logger.info("Getting pets for user: {}", user.getName());
        return new ResponseEntity<>(petService.getUserPets(user.getName()), HttpStatus.OK);
    }

}
