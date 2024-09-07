package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class PetController {

    private final PetService petService;
    private final Logger logger = LoggerFactory.getLogger(PetController.class);

    @GetMapping("/pets")
    public ResponseEntity<?> getPets(Principal user) {
        logger.info("Getting pets for user: {}", user.getName());
        return new ResponseEntity<>(petService.getUserPets(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(Principal user, @RequestBody PetDTO petDTO) {
        return new ResponseEntity<>(petService.addPet(user.getName(), petDTO), HttpStatus.OK);
    }

}
