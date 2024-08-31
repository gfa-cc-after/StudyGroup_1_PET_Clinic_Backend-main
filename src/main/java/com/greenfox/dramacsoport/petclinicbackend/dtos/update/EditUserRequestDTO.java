package com.greenfox.dramacsoport.petclinicbackend.dtos.update;

import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import jakarta.validation.constraints.*;

import java.util.Objects;

public class EditUserRequestDTO {
    private final @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
    @NotNull(message = "Please enter a valid email address!")
    @NotBlank(message = "Please enter a valid email address!") String email;
    private final @NotNull(message = "Please enter your original password!")
    @NotEmpty(message = "Please enter your original password!")
    @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD) String originalPassword;
    private final @NotNull(message = "Please enter a valid password!")
    @NotEmpty(message = "Please enter a valid password!")
    @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD) String password;
    private final @NotNull(message = "Please enter your display name!")
    @NotBlank(message = "Please enter your display name!")
    @Size(min = 3, message = "Display name must be at least 3 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Display name can only contain alphanumeric characters")
    @Size(max = 20, message = "Display name could not be longer than 20 characters") String username;

    public EditUserRequestDTO(
            @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID)
            @NotNull(message = "Please enter a valid email address!")
            @NotBlank(message = "Please enter a valid email address!")
            String email,

            @NotNull(message = "Please enter your original password!")
            @NotEmpty(message = "Please enter your original password!")
            @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD)
            String originalPassword,

            @NotNull(message = "Please enter a valid password!")
            @NotEmpty(message = "Please enter a valid password!")
            @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD)
            String password,

            @NotNull(message = "Please enter your display name!")
            @NotBlank(message = "Please enter your display name!")
            @Size(min = 3, message = "Display name must be at least 3 characters long")
            @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Display name can only contain alphanumeric characters")
            @Max(value = 20, message = "Display name could not be longer than 20 characters")
            String username
    ) {
        this.email = email;
        this.originalPassword = originalPassword;
        this.password = password;
        this.username = username;
    }

    public @Email(message = AppServiceErrors.EMAIL_FIELD_NOT_VALID) @NotNull(message = "Please enter a valid email " +
            "address!") @NotBlank(message = "Please enter a valid email address!") String email() {
        return email;
    }

    public @NotNull(message = "Please enter your original password!") @NotEmpty(message = "Please enter your original" +
            " password!") @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD) String originalPassword() {
        return originalPassword;
    }

    public @NotNull(message = "Please enter a valid password!") @NotEmpty(message = "Please enter a valid password!") @Size(min = 3, message = AppServiceErrors.SHORT_PASSWORD) String password() {
        return password;
    }

    public @NotNull(message = "Please enter your display name!") @NotBlank(message = "Please enter your display " +
            "name!") @Size(min = 3, message = "Display name must be at least 3 characters long") @Pattern(regexp =
            "^[a-zA-Z0-9]*$", message = "Display name can only contain alphanumeric characters") @Max(value = 20,
            message = "Display name could not be longer than 20 characters") String username() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EditUserRequestDTO) obj;
        return Objects.equals(this.email, that.email) &&
                Objects.equals(this.originalPassword, that.originalPassword) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, originalPassword, password, username);
    }

    @Override
    public String toString() {
        return "EditUserRequestDTO[" +
                "email=" + email + ", " +
                "originalPassword=" + originalPassword + ", " +
                "password=" + password + ", " +
                "username=" + username + ']';
    }

}
