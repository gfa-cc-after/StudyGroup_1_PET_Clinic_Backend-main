package com.greenfox.dramacsoport.petclinicbackend.dtos.pet;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PetListResponse {
    private List<PetDTO> pets;
}