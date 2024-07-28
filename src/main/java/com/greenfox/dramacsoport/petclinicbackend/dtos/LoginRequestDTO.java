package com.greenfox.dramacsoport.petclinicbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank(message = "Email field is required.") @NotNull(message = "Email field is required.") String email,
        @NotEmpty(message = "Password field is required.") @NotNull(message = "Password field is required.") String password) {
}
