package com.greenfox.dramacsoport.petclinicbackend.dtos.pet;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PetDTO {
    private String petName;
    private String petBreed;
    private String petSex;
    private LocalDate petBirthDate;
    private LocalDate lastCheckUp;
    private LocalDate nextCheckUp;
    private String specialCondition;
}