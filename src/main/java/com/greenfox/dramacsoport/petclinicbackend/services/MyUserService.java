package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

@RequiredArgsConstructor
@Service
public class MyUserService {

    private final MyUserRepository myUserRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String petClinicEmail;

    /**
     * Convert any user related DTO to the Entity object used by this Service
     * @param sourceDTO the DTO, you want to convert
     * @return MyUser entity
     */
    private <T> MyUser convertToEntity(T sourceDTO){
        return modelMapper.map(sourceDTO, MyUser.class);
    }

    public MyUser saveUser(MyUser user){
        return myUserRepository.save(user);
    }

    /** This method registers a new user.
     * Saves the input fields and encodes the password for storage.
     * After the entity has been created, the application sends a greeting email
     * to the e-mail address of the new user.
     *
     * @param userRequest the user object created from the registration form
     */
    public MyUser registerUser(RegisterRequestDTO userRequest){
        MyUser newUser = convertToEntity(userRequest);
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        sendEmailAfterRegistration(newUser);
        return saveUser(newUser);
    }

    public boolean isUserRegistered (String email){
        return myUserRepository.findByEmail(email).isPresent();
    }

    public boolean isPasswordLongerThanThreeChar(String password) {
        return password.length() > 3;
    }

    public boolean isPasswordMatching(String email, String password) {
        return passwordEncoder.matches(password, myUserRepository.findByEmail(email).orElseThrow().getPassword());
    }

    public boolean isMissingRegisterCredential(RegisterRequestDTO userDTO){
        return     userDTO.getEmail() == null || userDTO.getEmail().isEmpty()
                || userDTO.getUsername() == null || userDTO.getUsername().isEmpty()
                || userDTO.getPassword() == null || userDTO.getPassword().isEmpty();
    }

    private void sendEmailAfterRegistration(MyUser user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(petClinicEmail);
        message.setTo(user.getEmail());
        message.setSubject("Registration successful - Pet Clinic");
        message.setText("Dear " + user.getUsername()+ ",\n\n" +
                "Thank you for registering to our Pet Clinic application!\n\n" +
                "Best regards,\n" +
                "Pet Clinic Team");
        javaMailSender.send(message);
    }
}
