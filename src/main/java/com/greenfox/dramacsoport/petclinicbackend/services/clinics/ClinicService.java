package com.greenfox.dramacsoport.petclinicbackend.services.clinics;

import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicListResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.delete.DeleteClinicResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;

import javax.naming.NameAlreadyBoundException;

public interface ClinicService {
    ClinicListResponse getClinics(String email);
    ClinicDTO addClinic(String email, ClinicDTO clinicDTO) throws NameAlreadyBoundException;
    boolean isClinicRegistered(String name);
    DeleteClinicResponse deleteClinic(Long id) throws DeletionException;
}
