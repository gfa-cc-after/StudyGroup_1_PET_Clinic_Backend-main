package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AppUserServiceImplTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private AppUserServiceImpl appUserService;
    @Captor
    private ArgumentCaptor<SimpleMailMessage> captor;
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new AppUser( (long)2,"test@example.com", "testUser", "password", Role.USER);
    }

    @Test
    void sendEmailAfterRegistration_shouldSendEmailWhenNewUserIsRegistered() {
        appUserService.sendEmailAfterRegistration(testUser);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage actualMessage = captor.getValue();

        assertNotNull(actualMessage.getTo());
        assertEquals("test@example.com", actualMessage.getTo()[0]);
        assertEquals("Registration successful - Pet Clinic", actualMessage.getSubject());
        assertEquals("Dear testUser,\n\n" +
                        "Thank you for registering to our Pet Clinic application!\n\n" +
                        "Best regards,\n" +
                        "Pet Clinic Team",
                actualMessage.getText());
    }
    
//    @Test
//    void registerUser_shouldSendEmailConfirmationAfterSuccessfulRegistration() {
//        // Given
//        RegisterRequestDTO registerRequest = new RegisterRequestDTO("testUser", "test@example.com", "password");
//        AppUser expectedUser = new AppUser((long)2,"test@example.com", "testUser", "password", Role.USER);
//
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(javaMailSender.send(any(SimpleMailMessage.class))).thenReturn(null);
//
//        // When
//        AppUser actualUser = appUserService.registerUser(registerRequest);
//
//        // Then
//        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
//        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
//        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
//        assertEquals("encodedPassword", actualUser.getPassword());
//    }
}
