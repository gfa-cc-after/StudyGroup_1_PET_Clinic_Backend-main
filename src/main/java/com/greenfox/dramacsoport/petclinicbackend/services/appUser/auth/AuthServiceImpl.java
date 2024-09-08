package com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.controllers.appUser.auth.LoginController;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectLoginCredentialsException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.InvalidPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final JavaMailSender javaMailSender;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final AppUserService appUserService;

    @Value("${spring.mail.username}")
    private String petClinicEmail;

    /**
     * Convert any user related DTO to the Entity object used by this Service
     *
     * @param sourceDTO the DTO, you want to convert
     * @return MyUser entity
     */
    private <T> AppUser convertToEntity(T sourceDTO) {
        return modelMapper.map(sourceDTO, AppUser.class);
    }

    /**
     * <h3>This method registers a new user.</h3>
     * <ul>
     *     <li>If even one field is not filled, then throws an exception.</li>
     *     <li>If the password is only 3 characters long, throws an exception.</li>
     *     <li>If the user is already created, throws an exception.</li>
     * </ul>
     * Saves the input fields and encodes the password for storage.
     * After the entity has been created, the application sends a greeting email
     * to the e-mail address of the new user.
     *
     * @param userRequest the user object created from the registration form
     */
    public AppUser registerUser(RegisterRequestDTO userRequest) throws IncorrectPasswordException,
            NameAlreadyBoundException {

        if (!isPasswordLongerThanThreeChar(userRequest.getPassword())) {
            throw new InvalidPasswordException(AppServiceErrors.SHORT_PASSWORD);
        }
        if (isUserRegistered(userRequest.getEmail())) {
            throw new NameAlreadyBoundException(AppServiceErrors.USERNAME_ALREADY_EXISTS);
        }

        AppUser newUser = convertToEntity(userRequest);
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        try {
            sendEmailAfterRegistration(userRequest);
        } catch (Exception e) {
            logger.error("Failed to send email after registration: {}", e.getMessage());
        }

        return appUserRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO requestDTO) throws IncorrectLoginCredentialsException {
        if (!authenticateUser(requestDTO)) {
            throw new IncorrectLoginCredentialsException();
        }
        AppUser appUser = appUserService.loadUserByEmail(requestDTO.email());
        String token = jwtService.generateToken(appUser);
        return new LoginResponseDTO(token);
    }

    private boolean authenticateUser(LoginRequestDTO requestDTO) {
        try {
            return passwordEncoder.matches(
                    requestDTO.password(),
                    appUserService.loadUserByEmail(requestDTO.email()).getPassword()
            );
        } catch (UsernameNotFoundException ue) {
            throw new IncorrectLoginCredentialsException();
        }
    }

    private boolean isPasswordLongerThanThreeChar(String password) {
        return password.length() > 3;
    }

    private boolean isUserRegistered(String email) {
        return appUserRepository.existsByEmail(email);
    }

    public void sendEmailAfterRegistration(RegisterRequestDTO user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(petClinicEmail);
        message.setTo(user.getEmail());
        message.setSubject("Registration successful - Pet Clinic");
        message.setText("""
                Dear %s,

                Thank you for registering to our Pet Clinic application!

                Best regards,
                Pet Clinic Team""".formatted(user.getDisplayName()));
        javaMailSender.send(message);
        logger.info("Email sent successfully");
    }

}
