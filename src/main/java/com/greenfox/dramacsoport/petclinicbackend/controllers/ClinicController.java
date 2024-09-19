package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.services.clinics.ClinicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NameAlreadyBoundException;
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
    public ResponseEntity<?> addClinic(Principal user, @RequestBody ClinicDTO clinicDTO) throws NameAlreadyBoundException {
        logger.info("Adding new clinic: {}", clinicDTO.getName());
        return new ResponseEntity<>(clinicService.addClinic(user.getName(), clinicDTO), HttpStatus.OK);
    }

    @DeleteMapping("/clinic/{name}")
    public ResponseEntity<?> deleteClinic(@PathVariable String name) throws DeletionException {
        logger.info("Deleting clinic with the name: {}", name);
        return new ResponseEntity<>(clinicService.deleteClinic(name), HttpStatus.OK);
    }


}
