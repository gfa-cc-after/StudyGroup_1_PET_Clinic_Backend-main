package com.greenfox.dramacsoport.petclinicbackend.services.clinics;


import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicListResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.ClinicRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService{

    private ClinicRepository clinicRepository;
    private AppUserService appUserService;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ClinicListResponse getClinics(String email) {
        if(appUserService.loadUserByEmail(email).getRole().equals(Role.ADMIN)){
            List<Clinic> clinics = clinicRepository.findAll();
            List<ClinicDTO> clinicDTOList = clinics.stream()
                    .map(clinic -> modelMapper.map(clinic, ClinicDTO.class))
                    .collect(Collectors.toList());
            return new ClinicListResponse(clinicDTOList);
        }
        return null;
    }
}
