package com.greenfox.dramacsoport.petclinicbackend.dtos.update;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EditUserRequestDTO {

    @NotNull(message = "Please enter a valid email address!")
    @NotBlank(message = "Please enter a valid email address!")
    @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
    private final String email;

    @NotNull(message = "Please enter your original password!")
    @NotEmpty(message = "Please enter your original password!")
    private final String originalPassword;

    @Size(min = 4, message = AppServiceErrors.SHORT_PASSWORD)
    private final String password;

    @NotNull(message = "Please enter your display name!")
    @NotBlank(message = "Please enter your display name!")
    @Pattern(regexp = "^[\\p{L}0-9_-]*$", message = "Display name can only contain alphanumeric characters")
    @Size(max = 20, message = "Display name could not be longer than 20 characters")
    private final String displayName;


    public @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID) @NotNull(message = "Please enter a valid email " +
            "address!") @NotBlank(message = "Please enter a valid email address!") String email() {
        return email;
    }

    public @NotNull(message = "Please enter your original password!") @NotEmpty(message = "Please enter your original" +
            " password!") String originalPassword() {
        return originalPassword;
    }

    public @NotNull(message = "Please enter a valid password!") @NotEmpty(message = "Please enter a valid password!") @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD) String password() {
        return password;
    }

    public @NotNull(message = "Please enter your display name!") @NotBlank(message = "Please enter your display " +
            "name!") @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Display name can only contain alphanumeric " +
            "characters") @Size(max = 20, message = "Display name could not be longer than 20 characters") String displayName() {
        return displayName;
    }
}
