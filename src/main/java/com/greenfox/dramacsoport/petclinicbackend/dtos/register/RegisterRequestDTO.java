package com.greenfox.dramacsoport.petclinicbackend.dtos.register;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class RegisterRequestDTO {
    @NotBlank(message = "Display name field is required.")
    @NotNull(message = "Display name is required.")
    private final String displayName;

    @NotBlank(message = "Email field is required.")
    @NotNull(message = "Email field is required.")
    @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
    private final String email;

    @NotBlank(message = "Password field is required.")
    @NotNull(message = "Password field is required.")
    @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD)
    private final String password;

    private final Role role = Role.USER;
}
