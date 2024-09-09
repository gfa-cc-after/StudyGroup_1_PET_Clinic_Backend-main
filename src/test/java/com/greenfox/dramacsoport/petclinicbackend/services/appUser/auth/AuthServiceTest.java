package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.NameAlreadyBoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl appUserAuthService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserService appUserService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private JwtService jwtService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> captor;

    private RegisterRequestDTO registerRequestDTO;

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    public void setup() {
        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testUser", "test@example.com", "password");

        loginRequestDTO = new LoginRequestDTO("test@example.com","password");

        // Set up the email sender
        String petClinicEmail = "petclinic@example.com";
        System.setProperty("spring.mail.username", petClinicEmail);
    }

    @Test
    public void registerMethodIsSuccessfullyCalled() throws NameAlreadyBoundException {

        appUserAuthService.registerUser(registerRequestDTO);

        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }

    @Test
    void sendEmailAfterRegistration_shouldSendEmailWhenNewUserIsRegistered() {
        RegisterRequestDTO testUser = new RegisterRequestDTO("testUser", "test@example.com", "password");
        appUserAuthService.sendEmailAfterRegistration(testUser);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage actualMessage = captor.getValue();

        assertEquals("test@example.com", actualMessage.getTo()[0]);
        assertEquals("Registration successful - Pet Clinic", actualMessage.getSubject());
        assertEquals("""
                        Dear testUser,

                        Thank you for registering to our Pet Clinic application!

                        Best regards,
                        Pet Clinic Team""",
                actualMessage.getText());
    }

    @Test
    public void loginMethodIsSuccessfullyCalled() throws UsernameNotFoundException {
        // Arrange: Mock user details and token generation
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com"); // Ensure this matches the loginRequestDTO
        appUser.setPassword("encodedPassword");

        // Mock the behavior of finding a user by email
        when(appUserService.loadUserByEmail(loginRequestDTO.email())).thenReturn(appUser);
        // Mock the behavior of password matching
        when(passwordEncoder.matches(loginRequestDTO.password(), appUser.getPassword())).thenReturn(true);
        // Mock the behavior of JWT token generation
        when(jwtService.generateToken(any(AppUser.class))).thenReturn("mockedJwtToken");
        // Mock the behavior of role extraction

        // Act: Call the login method
        LoginResponseDTO token = appUserAuthService.login(loginRequestDTO);

        // Assert: Verify the token and interactions
        assertNotNull(token);
        assertEquals("mockedJwtToken", token.token()); // Ensure you're accessing the correct field
        verify(jwtService, times(1)).generateToken(any(AppUser.class));
    }

}
