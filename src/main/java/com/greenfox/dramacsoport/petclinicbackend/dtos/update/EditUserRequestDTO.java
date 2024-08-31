package com.greenfox.dramacsoport.petclinicbackend.dtos.update;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record EditUserRequestDTO(
        @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
        String newEmail,

        String prevPassword,

        @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD)
        String newPassword,

        @Size(max = 20, message = "Display name could not be longer than 20 characters")
        String newDisplayName
) {
}
