package com.greenfox.dramacsoport.petclinicbackend.dtos;

import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;

import java.util.List;

public record ClinicDTO(List<Clinic> clinics) {
}
