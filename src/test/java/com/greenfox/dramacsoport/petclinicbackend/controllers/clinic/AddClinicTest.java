package com.greenfox.dramacsoport.petclinicbackend.controllers.clinic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.dtos.clinic.ClinicDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import com.greenfox.dramacsoport.petclinicbackend.services.clinics.ClinicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddClinicTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClinicService clinicService;
    @Autowired
    private ObjectMapper objectMapper;
    private final ModelMapper modelMapper = new ModelMapper();
    private ClinicDTO clinicDTO;
    private Clinic clinic;

    @BeforeEach
    void setUp() {
        clinicDTO = new ClinicDTO();
        clinicDTO.setName("Clinic 1");
        clinicDTO.setAddress("Address 1");
        clinic = modelMapper.map(clinicDTO, Clinic.class);
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void shouldAddClinicSuccessfullyForAdmin() throws Exception {

        when(clinicService.addClinic(anyString(), any(ClinicDTO.class))).thenReturn(modelMapper.map(clinic, ClinicDTO.class));

        mockMvc.perform(post("/api/v1/admin/clinic")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clinicDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Clinic 1"))
                .andExpect(jsonPath("$.address").value("Address 1"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldReturnForbiddenForNonAdmin() throws Exception {

        mockMvc.perform(post("/api/v1/admin/clinic")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(clinicDTO)))
                .andExpect(status().isForbidden());
    }

}
