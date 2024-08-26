package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.naming.NameAlreadyBoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistratingUserTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDTO registerRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

        authService = new AuthServiceImpl(
                appUserRepository,
                passwordEncoder,
                jwtService,
                javaMailSender,
                new AppServiceErrors());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange: Mock that a user already exists in the repository
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(new AppUser()));

        // Act & Assert: Verify the exception and its message
        NameAlreadyBoundException exception = assertThrows(NameAlreadyBoundException.class, () -> authService.registerUser(registerRequestDTO));

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

        // Act & Assert: Verify the exception and its message
        PasswordException exception = assertThrows(PasswordException.class, () -> authService.registerUser(registerRequestDTO));

        // Assert that the message matches the expected message
        assertEquals("Password must be longer than 3 characters.", exception.getMessage());

        // Verify that no user is saved and no email is sent
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

}
