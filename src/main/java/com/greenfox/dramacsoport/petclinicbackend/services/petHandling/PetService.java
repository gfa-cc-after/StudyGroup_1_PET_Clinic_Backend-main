package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.PetListResponse;

public interface PetService {

    PetListResponse getUserPets(String email);
}
