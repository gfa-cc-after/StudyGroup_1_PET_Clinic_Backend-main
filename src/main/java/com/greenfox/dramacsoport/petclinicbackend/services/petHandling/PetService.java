package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.PetListResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PetService {

    PetListResponse getUserPets(String email);
}
