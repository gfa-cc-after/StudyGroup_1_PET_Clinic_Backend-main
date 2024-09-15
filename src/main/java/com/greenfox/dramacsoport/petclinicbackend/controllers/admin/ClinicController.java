package com.greenfox.dramacsoport.petclinicbackend.controllers.admin;

import com.greenfox.dramacsoport.petclinicbackend.dtos.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.repositories.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClinicController {

    @Autowired
    private ClinicRepository repo;

    @GetMapping("api/v1/admin/clinics")
    public ResponseEntity<?> loadClinics() {
        ClinicDTO dto = new ClinicDTO(repo.findAll());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
