package com.greenfox.dramacsoport.petclinicbackend.database;

import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;

import javax.naming.NameAlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RegistratingUserTest {

//
//
//    @Test
//    public void testRegisterUser() {
//        // Mock the behavior of password encoder and user repository
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
//
//        // Call the method to be
//        AppUser registeredUser = null;
//        try {
//            registeredUser = appUserService.registerUser(registerRequestDTO);
//        } catch (NameAlreadyBoundException e){
//            assertTrue(false ,"Initialization of username is wrong.");
//        }
//
//        // Verify interactions
//        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
//        verify(appUserRepository, times(1)).save(any(AppUser.class));
//        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
//
//        // Assert the results
//        assertEquals(registerRequestDTO.getEmail(), registeredUser.getEmail());
//        assertEquals("encodedPassword", registeredUser.getPassword());
//    }
//    //Unhappy cases
//
//    @Test
//    public void testPasswordLength() {
//
//        when(error.shortPassword()).thenReturn("Password must be longer than 3 characters.");
//
//
//        RegisterRequestDTO shortPasswordRequest = new RegisterRequestDTO("testuser",
//                "test@example.com",
//                "as"); // Shorter than 3 characters
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            appUserService.registerUser(shortPasswordRequest);
//        });
//
//        assertEquals("Password must be longer than 3 characters.", exception.getMessage());
//    }
//

}
