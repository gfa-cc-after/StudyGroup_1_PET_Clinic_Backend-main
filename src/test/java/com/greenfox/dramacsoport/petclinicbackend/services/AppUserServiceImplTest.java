package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
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
        testUser = new AppUser((long)2,"test@example.com", "testUser", "password", Role.USER, List.of(any(Pet.class)));
    }

    @Test
    void sendEmailAfterRegistration_shouldSendEmailWhenNewUserIsRegistered() {
        appUserService.sendEmailAfterRegistration(testUser);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage actualMessage = captor.getValue();

        assertEquals("test@example.com", actualMessage.getTo()[0]);
        assertEquals("Registration successful - Pet Clinic", actualMessage.getSubject());
        assertEquals("Dear testUser,\n\n" +
                        "Thank you for registering to our Pet Clinic application!\n\n" +
                        "Best regards,\n" +
                        "Pet Clinic Team",
                actualMessage.getText());
    }
}
