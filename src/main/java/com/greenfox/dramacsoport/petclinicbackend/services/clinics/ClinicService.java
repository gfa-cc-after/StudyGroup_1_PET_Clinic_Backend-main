package com.greenfox.dramacsoport.petclinicbackend.services.clinics;

import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicListResponse;

public interface ClinicService {
    ClinicListResponse getClinics(String email);
    ClinicDTO addClinic(String email, ClinicDTO clinicDTO);
}
