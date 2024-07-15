package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterDTO;
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

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String petClinicEmail;

    public MyUser convertToMyUser(RegisterDTO registerDTO) {
        return modelMapper.map(registerDTO, MyUser.class);
    }

    public RegisterDTO convertToRegisterDTO(MyUser myUser) {
        return modelMapper.map(myUser, RegisterDTO.class);
    }

    public boolean isUserRegistered(String email) {
        return myUserRepository.findByEmail(email).isPresent();
    }

    public boolean isPasswordLongerThanThreeChar(String password) {
        return password.length() > 3;
    }

    public boolean isPasswordMatching(String email, String password) {
        return passwordEncoder.matches(password, myUserRepository.findByEmail(email).get().getPassword());
    }

    public MyUser registerUser(RegisterDTO regUser) {
        return saveUser(convertToMyUser(regUser));
    }

    public MyUser saveUser(MyUser user) {
        sendEmailAfterRegistration(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    public boolean isMissingRegisterCredential(RegisterDTO user) {
        return user.email() == null || user.email().isEmpty()
                || user.username() == null || user.username().isEmpty()
                || user.password() == null || user.password().isEmpty();
    }

    private void sendEmailAfterRegistration(MyUser user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(petClinicEmail);
        message.setTo(user.getEmail());
        message.setSubject("Registration successful - Pet Clinic");
        message.setText("Dear " + user.getUsername() + ",\n\n" +
                "Thank you for registering to our Pet Clinic application!\n\n" +
                "Best regards,\n" +
                "Pet Clinic Team");
        javaMailSender.send(message);
    }
}
