package com.greenfox.dramacsoport.petclinicbackend.repositories;

import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
