package com.greenfox.dramacsoport.petclinicbackend.database;

import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.MyUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
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
    private MyUserRepository myUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MyUserService myUserService;

    private RegisterRequestDTO registerRequestDTO;
    private MyUser myUser;
    private final String petClinicEmail = "petclinic@example.com";

    @BeforeEach
    public void setup() {
        myUserService = new MyUserService(myUserRepository, passwordEncoder, javaMailSender);

        // Initialize test data
        registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

        myUser = new MyUser();
        myUser.setEmail("test@example.com");
        myUser.setUsername("testuser");
        myUser.setPassword("encodedPassword");

        // Set up the email sender
        System.setProperty("spring.mail.username", petClinicEmail);
    }

    @Test
    public void testRegisterUser() {
        // Mock the behavior of password encoder and user repository
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(myUserRepository.save(any(MyUser.class))).thenReturn(myUser);

        // Call the method to be tested
        MyUser registeredUser = myUserService.registerUser(registerRequestDTO);

        // Verify interactions
        verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());
        verify(myUserRepository, times(1)).save(any(MyUser.class));
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        // Assert the results
        assertEquals(registerRequestDTO.getEmail(), registeredUser.getEmail());
        assertEquals("encodedPassword", registeredUser.getPassword());
    }
}
