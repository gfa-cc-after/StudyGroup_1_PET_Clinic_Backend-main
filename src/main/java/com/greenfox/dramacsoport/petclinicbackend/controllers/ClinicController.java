package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.clinics.ClinicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
public class ClinicController {

    private final ClinicService clinicService;
    private final Logger logger = LoggerFactory.getLogger(PetController.class);

    @GetMapping("/clinics")
    public ResponseEntity<?> getClinics(Principal user){
        logger.info("Getting clinics list for admin: {}", user.getName());
        return new ResponseEntity<>(clinicService.getClinics(user.getName()), HttpStatus.OK);
    }

    @PostMapping("/clinic")
    public ResponseEntity<?> addClinic(Principal user, @RequestBody ClinicDTO clinicDTO){
        logger.info("Adding new clinic: {}", clinicDTO.getName());
        return new ResponseEntity<>(clinicService.addClinic(user.getName(), clinicDTO), HttpStatus.OK);
    }
}
