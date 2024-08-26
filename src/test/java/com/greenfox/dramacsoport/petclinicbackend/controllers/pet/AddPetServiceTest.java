package com.greenfox.dramacsoport.petclinicbackend.controllers.pet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthServiceImpl;
import com.greenfox.dramacsoport.petclinicbackend.services.petHandling.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class AddPetServiceTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private AuthServiceImpl appUserAuthService;
    @InjectMocks
    private PetServiceImpl petService;

    private ModelMapper modelMapper = new ModelMapper();
    private PetDTO petDTO;
    private Pet pet;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        petDTO = new PetDTO();
        petDTO.setPetName("Max");
        petDTO.setPetBreed("dog");
        petDTO.setPetSex("Male");
        petDTO.setPetBirthDate(LocalDate.of(2024, 1, 1));

        appUser = new AppUser();
        appUser.setEmail("xy@example.com");
        appUser.setPassword("password");

        pet = modelMapper.map(petDTO, Pet.class);
        pet.setOwner(appUser);
    }

    @Test
    void shouldAddPetSuccessfully() {

        when(appUserAuthService.loadUserByUsername(anyString())).thenReturn(appUser);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        Pet savedPet = petService.addPet("xy@example.com", petDTO);

        assertEquals("Max", savedPet.getPetName());
        assertEquals("dog", savedPet.getPetBreed());
        assertEquals(appUser, savedPet.getOwner());

        verify(appUserAuthService).loadUserByUsername("xy@example.com");
        verify(petRepository).save(any(Pet.class));
    }
}

