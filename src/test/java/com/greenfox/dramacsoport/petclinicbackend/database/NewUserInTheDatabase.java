package com.greenfox.dramacsoport.petclinicbackend.database;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;

import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;

import com.greenfox.dramacsoport.petclinicbackend.services.AppUserService;

import com.greenfox.dramacsoport.petclinicbackend.services.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewUserInTheDatabase {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private RegisterRequestDTO registerRequestDTO;
    private AppUser appUser;
    private final String petClinicEmail = "petclinic@example.com";

    @BeforeEach
    public void setup() {
        appUserService = new AppUserServiceImpl(appUserRepository, passwordEncoder, jwtService, javaMailSender);

        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

        appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setUsername("testuser");
        appUser.setPassword("encodedPassword");


        // Set up the email sender
        System.setProperty("spring.mail.username", petClinicEmail);
    }

    @Test
    public void testRegisterUser() {
        // Mock the behavior of password encoder and user repository
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        // Call the method to be tested
        AppUser registeredUser = appUserService.registerUser(registerRequestDTO);

        // Verify interactions
        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        // Assert the results
        assertEquals(registerRequestDTO.getEmail(), registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
    }
}
