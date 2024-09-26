package com.greenfox.dramacsoport.petclinicbackend.dtos.pet;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetDTO {
    private Long id;

    @NotBlank(message = "Pet Name field is required.")
    @Pattern(regexp = "^[\\p{L}0-9_-]*$", message = "Name can only contain alphanumeric characters")
    private String petName;

    @NotBlank(message = "Pet Breed field is required.")
    private String petBreed;

    @NotBlank(message = "Pet Sex field is required.")
    private String petSex;

    @PastOrPresent(message = "Pet Birthdate should be a date in the past or the present day.")
    private LocalDate petBirthDate;

    @PastOrPresent(message = "Last Checkup should be a date in the past or the present day.")
    private LocalDate lastCheckUp;

    @Future(message = "Next Checkup should be a date in the future.")
    private LocalDate nextCheckUp;

    private String specialCondition;
}