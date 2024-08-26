package com.greenfox.dramacsoport.petclinicbackend.repositories;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email) throws UsernameNotFoundException;

    boolean existsByEmail(String email) throws UsernameNotFoundException;
}
