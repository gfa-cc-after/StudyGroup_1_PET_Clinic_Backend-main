package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.dtos.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUserTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        loginRequestDTO = new LoginRequestDTO("test@example.com", "password");

        appUserService = new AppUserServiceImpl(
                appUserRepository,
                passwordEncoder,
                jwtService,
                javaMailSender,
                new AppServiceErrors());

    }

    @Test
    public void loginMethodFailsWithWrongEmail() {
        // Arrange: Set up a non-matching email scenario
        when(appUserRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.empty());

        // Act & Assert: Expect an exception due to email mismatch
        assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.login(loginRequestDTO);
        });

        // Verify no token is generated since login should fail
        verify(jwtService, never()).generateToken(any(AppUser.class));
    }

    @Test
    public void loginMethodFailsWithWrongPassword() {
        // Arrange: Mock user with correct email but wrong password
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setPassword("encodedPassword");

        when(appUserRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.of(appUser));
        when(passwordEncoder.matches(loginRequestDTO.password(), appUser.getPassword())).thenReturn(false);

        // Act & Assert: Expect an exception due to password mismatch
       UsernameNotFoundException exception =  assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.login(loginRequestDTO);
        });

        assertEquals("Authentication failed! User not found.", exception.getMessage());

        // Verify no token is generated since login should fail
        verify(jwtService, never()).generateToken(any(AppUser.class));
    }
}
