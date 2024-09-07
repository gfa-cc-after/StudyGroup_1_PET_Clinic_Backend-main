package com.greenfox.dramacsoport.petclinicbackend.dtos.pet;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetDTO {
    @NotNull(message = "Pet Name field is required.")
    @NotBlank(message = "Pet Name field is required.")
    private String petName;

    @NotNull(message = "Pet Breed field is required.")
    @NotBlank(message = "Pet Breed field is required.")
    private String petBreed;

    @NotNull(message = "Pet Sex field is required.")
    @NotBlank(message = "Pet Sex field is required.")
    private String petSex;

    @NotNull(message = "Pet Birthdate field is required.")
    @NotBlank(message = "Pet Birthdate field is required.")
    @PastOrPresent(message = "Pet Birthdate should be a date in the past or the present day.")
    private LocalDate petBirthDate;

    @PastOrPresent(message = "Last Checkup should be a date in the past or the present day.")
    private LocalDate lastCheckUp;

    @Future(message = "Next Checkup should be a date in the future.")
    private LocalDate nextCheckUp;

    private String specialCondition;
}