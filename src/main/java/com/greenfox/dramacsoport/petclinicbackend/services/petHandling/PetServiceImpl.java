package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetListResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.update.EditPetResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final AppUserService appUserService;
    private final PetRepository petRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    @Override
    public PetListResponse getUserPets(String email) {
        List<Pet> petList = petRepository.findAllByOwnerId(appUserService.loadUserByEmail(email).getId());

        List<PetDTO> petDTOList = petList.stream()
                .map(pet -> modelMapper.map(pet, PetDTO.class))
                .collect(Collectors.toList());

        return new PetListResponse(petDTOList);
    }

    @Override
    public PetDTO addPet(String email, PetDTO petDTO) {
        Pet pet = modelMapper.map(petDTO, Pet.class);
        pet.setOwner(appUserService.loadUserByEmail(email));
        petRepository.save(pet);
        return modelMapper.map(pet, PetDTO.class);
    }

    @Override
    public EditPetResponse changePetData(String email, PetDTO petDTO) {
        Pet actualPet = petRepository.findPetById(petDTO.getId());

        if (!actualPet.getOwner().getEmail().equals(email)) {
            logger.error("This pet is NOT related to this user. Pet entity cannot be updated.");
            throw new UnauthorizedActionException("This pet is NOT related to this user...not allowed to update pet details");
        }

        modelMapper.map(petDTO, actualPet);
        petRepository.save(actualPet);

        logger.info("Pet entity mapped and updated.");

        return new EditPetResponse("Updated pet data saved.");
    }
}
