package com.greenfox.dramacsoport.petclinicbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PetListResponse {
    private String token;
    private List<PetDTO> pets;
}