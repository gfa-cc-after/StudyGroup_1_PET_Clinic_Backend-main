package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetListResponse;

public interface PetService {

    PetListResponse getUserPets(String email);

    PetDTO addPet(String email, PetDTO petDTO);
}
