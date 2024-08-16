package com.greenfox.dramacsoport.petclinicbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AppUserDTO {
    private String email;
    private List<PetDTO> pets;
}