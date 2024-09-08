package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectLoginCredentialsException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUserTest {

    @Mock
    private AppUserService appUserService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        loginRequestDTO = new LoginRequestDTO("test@example.com", "password");
    }

    @Test
    public void loginMethodFailsWithWrongEmail() {
        // Arrange: Set up a non-matching email scenario
        when(appUserService.loadUserByEmail(loginRequestDTO.email())).thenThrow(UsernameNotFoundException.class);

        // Act & Assert: Expect an exception due to email mismatch
        assertThrows(IncorrectLoginCredentialsException.class, () -> authService.login(loginRequestDTO));

        // Verify no token is generated since login should fail
        verify(jwtService, never()).generateToken(any(AppUser.class));
    }

    @Test
    public void loginMethodFailsWithWrongPassword() {
        // Arrange: Mock user with correct email but wrong password
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        appUser.setPassword("encodedPassword");

        when(appUserService.loadUserByEmail(loginRequestDTO.email())).thenReturn(appUser);
        when(passwordEncoder.matches(loginRequestDTO.password(), appUser.getPassword())).thenReturn(false);

        // Act & Assert: Expect an exception due to password mismatch
        IncorrectLoginCredentialsException exception = assertThrows(IncorrectLoginCredentialsException.class,
                () -> authService.login(loginRequestDTO));

        assertEquals(AppServiceErrors.AUTHENTICATION_FAILED_BAD_CREDENTIALS, exception.getMessage());

        // Verify no token is generated since login should fail
        verify(jwtService, never()).generateToken(any(AppUser.class));
    }
}
