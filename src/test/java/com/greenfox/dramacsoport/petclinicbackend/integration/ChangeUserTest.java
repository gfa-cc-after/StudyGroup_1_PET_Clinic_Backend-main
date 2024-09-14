package com.greenfox.dramacsoport.petclinicbackend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ChangeUserTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder pwEncoder;

    @Autowired
    AppUserRepository userRepo;

    @Autowired
    PetRepository petRepository;

    @BeforeEach
    public void setup() {
        AppUser user = AppUser.builder()
                .email("user@test.com")
                .password(pwEncoder.encode("password"))
                .displayName("User")
                .role(Role.USER)
                .build();

        AppUser savedUser = userRepo.save(user);
        System.out.println("PW encoded after initializing user in test: " + user.getPassword());

        Pet pet1 = new Pet();
        pet1.setPetName("Bodri");
        pet1.setOwner(savedUser);
        Pet savedPet1 = petRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setPetName("Cirmi");
        pet2.setOwner(savedUser);
        Pet savedPet2 = petRepository.save(pet2);

        System.out.println(userRepo.findByEmail(user.getEmail()).orElse(null).getPets());
    }

    @Test
    @DisplayName("Change user data - HAPPY path")
    @WithMockUser(username = "user@test.com")
    public void shouldUpdateUserData() throws Exception {
        //ARRANGE
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "new@test.com",
                "password",
                "new_Password",
                "NewName"
        );
        System.out.println("PW encoded from DTO after initializing in test: " + pwEncoder.encode(requestDTO.getPassword()));
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        //ACT
        mockMvc.perform(post("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"New user data saved.\"}"));

        //ASSERT
        //user logged out
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        //check for leftovers
        assertFalse(userRepo.existsByEmail("user@test.com"));
        AppUser updatedUser = userRepo.findByEmail("new@test.com").orElse(null);
        assertNotNull(updatedUser);

        //check new data
        assertEquals(requestDTO.email(), updatedUser.getEmail());
        System.out.println(("encoded new PW back in test: %s".formatted(pwEncoder.encode(updatedUser.getPassword()))));
        assertTrue(pwEncoder.matches(requestDTO.password(), updatedUser.getPassword()));
        assertEquals(requestDTO.displayName(), updatedUser.getDisplayName());
        assertEquals(Role.USER, updatedUser.getRole());
        Hibernate.initialize(updatedUser.getPets());
        assertEquals(2, updatedUser.getPets().size());
    }
}
