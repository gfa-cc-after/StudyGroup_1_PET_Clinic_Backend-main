package com.greenfox.dramacsoport.petclinicbackend.service;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.dtos.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;

import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;

import com.greenfox.dramacsoport.petclinicbackend.services.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.NameAlreadyBoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

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

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

        loginRequestDTO = new LoginRequestDTO("test@example.com","password");

        // Set up the email sender
        String petClinicEmail = "petclinic@example.com";
        System.setProperty("spring.mail.username", petClinicEmail);
    }

    @Test
    public void registerMethodIsSuccessfullyCalled() throws PasswordException, NameAlreadyBoundException {

        appUserService.registerUser(registerRequestDTO);

        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }

    @Test
    public void loginMethodIsSuccessfullyCalled() throws UsernameNotFoundException {
        // Arrange: Mock user details and token generation
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com"); // Ensure this matches the loginRequestDTO
        appUser.setPassword("encodedPassword");

        // Mock the behavior of finding a user by email
        when(appUserRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.of(appUser));
        // Mock the behavior of password matching
        when(passwordEncoder.matches(loginRequestDTO.password(), appUser.getPassword())).thenReturn(true);
        // Mock the behavior of JWT token generation
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mockedJwtToken");

        // Act: Call the login method
        String token = appUserService.login(loginRequestDTO);

        // Assert: Verify the token and interactions
        assertNotNull(token);
        assertEquals("mockedJwtToken", token);
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }


}
