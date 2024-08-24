package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetListResponse;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{
    private final AuthServiceImpl appUserAuthService;
    private final PetRepository petRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public PetListResponse getUserPets(String email) {
        List<Pet> petList=  petRepository.findAllByOwnerId(appUserAuthService.loadUserByUsername(email).getId());

        List<PetDTO> petDTOList = petList.stream()
                .map(pet -> modelMapper.map(pet, PetDTO.class))
                .collect(Collectors.toList());

        return new PetListResponse(petDTOList);
    }

    @Override
    public Pet addPet(String email, PetDTO petDTO) {
        Pet pet = modelMapper.map(petDTO,Pet.class);
        pet.setOwner(appUserAuthService.loadUserByUsername(email));
        petRepository.save(pet);
        return pet;
    }
}
