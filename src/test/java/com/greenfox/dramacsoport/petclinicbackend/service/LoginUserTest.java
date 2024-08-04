package com.greenfox.dramacsoport.petclinicbackend.service;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.NameAlreadyBoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LoginUserTest {

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

        @BeforeEach
        public void setup() {
            // Initialize test data
            registerRequestDTO = new RegisterRequestDTO("testuser", "test@example.com","password");

            appUserService = new AppUserServiceImpl(
                    appUserRepository,
                    passwordEncoder,
                    jwtService,
                    javaMailSender,
                    new AppServiceErrors());
        }

    NameAlreadyBoundException exception = assertThrows(NameAlreadyBoundException.class, () -> {
        appUserService.registerUser(registerRequestDTO);
    });

    }
