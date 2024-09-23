package com.greenfox.dramacsoport.petclinicbackend.dtos.clinic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClinicListResponse {
    private List<ClinicDTO> clinics;
}
