package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.user.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.naming.NameAlreadyBoundException;

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
    private JavaMailSender javaMailSender;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDTO registerRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange: Mock that a user already exists in the repository
        when(appUserRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert: Verify the exception and its message
        NameAlreadyBoundException exception = assertThrows(NameAlreadyBoundException.class, () -> authService.registerUser(registerRequestDTO));

        // Assert that the message matches the expected message
        assertEquals(AppServiceErrors.USERNAME_ALREADY_EXISTS, exception.getMessage());

        // Verify that the repository's save method and the email sender's send method are never called
        verify(appUserRepository, times(1)).existsByEmail("test@example.com");
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testRegisterUser_ShortPassword() {
        // Arrange: Create a DTO with a short password
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","p");

        // Act & Assert: Verify the exception and its message
        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class,
                () -> authService.registerUser(registerRequestDTO));

        // Assert that the message matches the expected message
        assertEquals(AppServiceErrors.SHORT_PASSWORD, exception.getMessage());

        // Verify that no user is saved and no email is sent
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
    }

}
