package com.greenfox.dramacsoport.petclinicbackend.dtos;

import com.greenfox.dramacsoport.petclinicbackend.models.Role;

public record LoginResponseDTO (String token, String role) {
}
