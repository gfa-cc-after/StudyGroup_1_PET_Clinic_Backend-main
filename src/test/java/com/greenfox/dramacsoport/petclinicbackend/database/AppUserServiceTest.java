package com.greenfox.dramacsoport.petclinicbackend.database;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.NameAlreadyBoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    private AppServiceErrors error;

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
        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

        // Set up the email sender
        System.setProperty("spring.mail.username", petClinicEmail);

        appUserRepository = Mockito.mock(AppUserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JwtService.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);


        appUserService = new AppUserServiceImpl(
                appUserRepository,
                passwordEncoder,
                jwtService,
                javaMailSender,
                new AppServiceErrors());
    }

    @Test
    public void registerMethodIsSuccessfullyCalled() throws PasswordException, NameAlreadyBoundException {

        appUserService.registerUser(registerRequestDTO);

        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }
    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange: Mock that a user already exists in the repository
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(new AppUser()));

        // Act & Assert: Verify the exception and its message
        NameAlreadyBoundException exception = assertThrows(NameAlreadyBoundException.class, () -> {
            appUserService.registerUser(registerRequestDTO);
        });

        // Assert that the message matches the expected message
        assertEquals("User already exists.", exception.getMessage());

        // Verify that the repository's save method and the email sender's send method are never called
        verify(appUserRepository, times(1)).findByEmail("test@example.com");
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testRegisterUser_ShortPassword() {
        // Arrange: Create a DTO with a short password
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","p");
        // Set a short password

        // Act & Assert: Verify the exception and its message
        PasswordException exception = assertThrows(PasswordException.class, () -> {
            appUserService.registerUser(registerRequestDTO);
        });

        // Assert that the message matches the expected message
        assertEquals("Password must be longer than 3 characters.", exception.getMessage());

        // Verify that no user is saved and no email is sent
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

}
