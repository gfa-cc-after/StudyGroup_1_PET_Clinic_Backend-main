package com.greenfox.dramacsoport.petclinicbackend.dtos;

import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import lombok.Data;

@Data
public final class RegisterRequestDTO {
    private final String username;
    private final String email;
    private final String password;
    private final Role role = Role.USER;
}
