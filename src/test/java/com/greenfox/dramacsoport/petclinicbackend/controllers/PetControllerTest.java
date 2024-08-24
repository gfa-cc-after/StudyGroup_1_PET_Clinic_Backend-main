package com.greenfox.dramacsoport.petclinicbackend.controllers;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.Arrays;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void setUp() {
        // Set up mock data
        AppUser userWithPets = new AppUser();
        userWithPets.setEmail("userWithPets@example.com");
        userWithPets.setPassword("Password");
        AppUser userWithNoPets = new AppUser();
        userWithNoPets.setEmail("userWithNoPets@example.com");
        userWithNoPets.setPassword("Password");

        appUserRepository.saveAll(Arrays.asList(userWithPets, userWithNoPets));

        Pet pet1 = new Pet();
        pet1.setPetName("Buddy");
        pet1.setOwner(userWithPets);
        Pet pet2 = new Pet();
        pet2.setPetName("Max");
        pet2.setOwner(userWithPets);

        petRepository.saveAll(Arrays.asList(pet1, pet2));
    }

    @Test
    @WithMockUser(username = "userWithPets@example.com")
    public void testCorrectEmailWithExistingPets() throws Exception {
        mockMvc.perform(get("/api/v1/user/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pets[0].petName").value("Buddy"))
                .andExpect(jsonPath("$.pets[1].petName").value("Max"));
    }

    @Test
    @WithMockUser(username = "userWithNoPets@example.com")
    public void testCorrectEmailWithNoExistingPets() throws Exception {
        mockMvc.perform(get("/api/v1/user/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pets").isEmpty());
    }

    @Test
    @WithMockUser(username = "nonExistingUser@example.com")
    public void testIncorrectEmail() throws Exception {
        mockMvc.perform(get("/api/v1/user/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

