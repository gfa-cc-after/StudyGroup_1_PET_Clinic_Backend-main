package com.greenfox.dramacsoport.petclinicbackend.dtos.login;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotBlank(message = "Email field is required.")
        @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
        String email,

        @NotEmpty(message = "Password field is required.")
        String password) {
}
