package com.greenfox.dramacsoport.petclinicbackend.controllers.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddPetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetService petService;
    @Autowired
    private ObjectMapper objectMapper;
    private final ModelMapper modelMapper = new ModelMapper();
    private PetDTO petDTO;
    private Pet pet;

    @BeforeEach
    void setUp() {
        petDTO = new PetDTO();
        petDTO.setPetName("Max");
        petDTO.setPetBreed("dog");
        petDTO.setPetSex("Male");
        petDTO.setPetBirthDate(LocalDate.of(2024, 1, 1));
        pet = modelMapper.map(petDTO, Pet.class);
    }

    @Test
    @WithMockUser(username = "xy@example.com")
    void shouldAddPetSuccessfully() throws Exception {

        when(petService.addPet(anyString(), any(PetDTO.class))).thenReturn(modelMapper.map(pet, PetDTO.class));

        mockMvc.perform(post("/api/v1/user/pet")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(petDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petName").value("Max"))
                .andExpect(jsonPath("$.petBreed").value("dog"));
    }
}
