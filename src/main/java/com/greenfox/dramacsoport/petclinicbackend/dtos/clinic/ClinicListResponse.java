package com.greenfox.dramacsoport.petclinicbackend.dtos.clinic;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ClinicListResponse {
    private List<ClinicDTO> clinics;
}
