package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.services.clinics.ClinicService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
public class ClinicController {

    private final ClinicService clinicService;
    private final Logger logger = LoggerFactory.getLogger(PetController.class);


}
