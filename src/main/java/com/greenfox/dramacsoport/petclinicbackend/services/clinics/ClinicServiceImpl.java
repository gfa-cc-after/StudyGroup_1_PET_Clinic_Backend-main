package com.greenfox.dramacsoport.petclinicbackend.services.clinics;


import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicListResponse;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import com.greenfox.dramacsoport.petclinicbackend.repositories.ClinicRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;
    private final AppUserService appUserService;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ClinicListResponse getClinics(String email) {
        List<Clinic> clinics = clinicRepository.findAll();
        List<ClinicDTO> clinicDTOList = clinics.stream()
                .map(clinic -> modelMapper.map(clinic, ClinicDTO.class))
                .collect(Collectors.toList());
        return new ClinicListResponse(clinicDTOList);
    }

    @Override
    public ClinicDTO addClinic(String email, ClinicDTO clinicDTO) throws NameAlreadyBoundException {
        if(isClinicRegistered(clinicDTO.getName())){
            throw new NameAlreadyBoundException(AppServiceErrors.CLINIC_ALREADY_EXISTS);
        }
        Clinic clinic = modelMapper.map(clinicDTO, Clinic.class);
        clinicRepository.save(clinic);
        return modelMapper.map(clinic, ClinicDTO.class);
    }

    @Override
    public boolean isClinicRegistered(String name) {
        return clinicRepository.existsByName(name);
    }


}
