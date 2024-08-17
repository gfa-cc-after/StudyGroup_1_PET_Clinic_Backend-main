package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.PetListResponse;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{
    private final AppUserServiceImpl appUserService;
    private final JwtService jwtService;

    private final ModelMapper modelMapper = new ModelMapper();
    @Override
    public PetListResponse getUserPets(String email) {
        List<Pet> petList=  appUserService.loadUserByUsername(email).getPets();

        List<PetDTO> petDTOList = petList.stream()
                .map(pet -> modelMapper.map(pet, PetDTO.class)).collect(Collectors.toList());


        System.out.println(petDTOList.getFirst().getPetName());


        return new PetListResponse(petDTOList);
    }
}
