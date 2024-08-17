package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository repository;

    /**
     * This User is an implementation of the built-in interface UserDetails (NOT our AppUser)
     * @param email the user's email, that will be used as username from now on.
     * @return UserDetails Object with email as username
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
