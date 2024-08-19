package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exeptions.PasswordException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final JavaMailSender javaMailSender;

    private final AppServiceErrors error;

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

    @Override
    public AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    /**
     * <h3>Looks for an entity in the storage and gives back a UserDetails object made from it.</h3>
     *
     * @param email the email, that is used as a username
     * @return an implementation of the security core UserDetails interface, NOT the same as the AppUser Entity
     * @throws UsernameNotFoundException when no entity found under this email.
     */
    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(error.usernameNotFound(email)));
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
    @Override
    public AppUser registerUser(RegisterRequestDTO userRequest) throws PasswordException, NameAlreadyBoundException {

        if (!isPasswordLongerThanThreeChar(userRequest.getPassword())) {
            throw new PasswordException(error.shortPassword());
        }
        if (isUserRegistered(userRequest.getEmail())) {
            throw new NameAlreadyBoundException(error.userAlreadyExists());
        }

        AppUser newUser = convertToEntity(userRequest);
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        try {
            sendEmailAfterRegistration(userRequest);
        } catch (Exception e) {
        }

        return saveUser(newUser);
    }


    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) throws UsernameNotFoundException {
        if (authenticateUser(requestDTO)) {
            AppUser appUser = loadUserByUsername(requestDTO.email());
            String token = jwtService.generateToken(appUser);
            return new LoginResponseDTO(token);
        }
        throw new UsernameNotFoundException(error.notFound());
    }

    private boolean authenticateUser(LoginRequestDTO requestDTO) {
        return passwordEncoder.matches(
                requestDTO.password(),
                loadUserByUsername(requestDTO.email()).getPassword()
        );
    }

    private boolean isPasswordLongerThanThreeChar(String password) {
        return password.length() > 3;
    }

    private boolean isUserRegistered(String email) {
        return appUserRepository.findByEmail(email).isPresent();
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
    }

}
