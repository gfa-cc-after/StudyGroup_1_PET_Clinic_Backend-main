package com.greenfox.dramacsoport.petclinicbackend.dtos.update;

public record EditUserRequestDTO(
        String email,
        String prevPassword,
        String newPassword,
        String displayName
) {
}
