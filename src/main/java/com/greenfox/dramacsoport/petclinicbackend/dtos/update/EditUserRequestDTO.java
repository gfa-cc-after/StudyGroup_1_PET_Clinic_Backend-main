package com.greenfox.dramacsoport.petclinicbackend.dtos.update;

import jakarta.validation.constraints.Size;

public record EditUserRequestDTO(
        String email,
        String prevPassword,
        @Size(min = 3, message = "New password must be at least 3 characters long.")
        String newPassword,
        String displayName
) {
}
