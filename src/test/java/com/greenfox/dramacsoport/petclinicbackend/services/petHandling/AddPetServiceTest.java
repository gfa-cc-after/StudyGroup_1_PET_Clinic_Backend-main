package com.greenfox.dramacsoport.petclinicbackend.services.petHandling;

import com.greenfox.dramacsoport.petclinicbackend.dtos.pet.PetDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddPetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AppUserRepository appUserRepository;

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

        when(appUserRepository.findByEmail(anyString())).thenReturn(appUser);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        PetDTO savedPetDTO = petService.addPet("xy@example.com", petDTO);

        assertEquals(petDTO.getPetName(), savedPetDTO.getPetName());
        assertEquals(petDTO.getPetBreed(), savedPetDTO.getPetBreed());

        verify(appUserRepository).findByEmail("xy@example.com");

        ArgumentCaptor<Pet> petCaptor = forClass(Pet.class);
        verify(petRepository).save(petCaptor.capture());
        Pet actualPet = petCaptor.getValue();

        assertEquals(petDTO.getPetName(), actualPet.getPetName());
        assertEquals(petDTO.getPetBreed(), actualPet.getPetBreed());
        assertEquals(appUser, actualPet.getOwner());

    }

}

