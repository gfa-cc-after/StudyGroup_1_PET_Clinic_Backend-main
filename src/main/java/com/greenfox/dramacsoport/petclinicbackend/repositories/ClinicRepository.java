package com.greenfox.dramacsoport.petclinicbackend.repositories;

import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    boolean existsByName(String name);
    Clinic findByName(String name);
}
