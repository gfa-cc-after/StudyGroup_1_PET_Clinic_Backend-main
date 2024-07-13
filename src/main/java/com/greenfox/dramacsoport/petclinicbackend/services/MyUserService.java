package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.MyUserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {
    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUserRegistered (String email){
        return myUserRepository.findByEmail(email).isPresent();
    }

    public MyUser returnUser (String email){
        return myUserRepository.findByEmail(email).get();
    }

    public boolean isPasswordLongerThanThreeChar(String password){
        return password.length() > 3;
    }

    public MyUser saveUser(MyUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }

    public boolean isMissingRegisterCredential(MyUser user){
        return     user.getEmail() == null || user.getEmail().isEmpty()
                || user.getUsername() == null || user.getUsername().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty();
    }

    public boolean isMissingLoginCredential(MyUser user){
        return     user.getEmail() == null || user.getEmail().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty();
    }
}
