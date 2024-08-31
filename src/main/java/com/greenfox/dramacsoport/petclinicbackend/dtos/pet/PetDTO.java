package com.greenfox.dramacsoport.petclinicbackend.dtos.pet;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetDTO {
    @NotNull
    @NotBlank
    private String petName;

    @NotNull
    @NotBlank
    private String petBreed;

    @NotNull
    @NotBlank
    private String petSex;

    @NotNull
    @NotBlank
    @PastOrPresent
    private LocalDate petBirthDate;

    @PastOrPresent
    private LocalDate lastCheckUp;

    @Future
    private LocalDate nextCheckUp;

    private String specialCondition;
}