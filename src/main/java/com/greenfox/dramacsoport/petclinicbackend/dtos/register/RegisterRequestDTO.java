package com.greenfox.dramacsoport.petclinicbackend.dtos.register;

import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class RegisterRequestDTO {
    @NotBlank(message = "Username field is required.")
    @NotNull(message = "Username field is required.")
    private final String displayName;
    @NotBlank(message = "Email field is required.")
    @NotNull(message = "Email field is required.")
    @Email
    private final String email;
    @NotBlank(message = "Password field is required.")
    @NotNull(message = "Password field is required.")
    @Size(min = 3, message = "Password must be at least 3 characters long.")
    private final String password;
    private final Role role = Role.USER;
}
