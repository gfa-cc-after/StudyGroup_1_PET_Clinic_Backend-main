package com.greenfox.dramacsoport.petclinicbackend.services.clinics;


import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicListResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteClinicResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import com.greenfox.dramacsoport.petclinicbackend.repositories.ClinicRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

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

    @Override
    public DeleteClinicResponse deleteClinic(String name) throws DeletionException {
        Clinic clinic = clinicRepository.findByName(name);
        if(clinicRepository.existsByName(name)) {
            clinicRepository.delete(clinic);
            logger.info("Clinic deleted with name: {}", name);
            return new DeleteClinicResponse(name + " clinic has been successfully deleted.");
        } else {
            throw new DeletionException("Unable to delete clinic with name: " + name);
        }
    }


}
