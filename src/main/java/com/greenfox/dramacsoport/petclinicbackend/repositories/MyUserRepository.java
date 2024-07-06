package com.greenfox.dramacsoport.petclinicbackend.repositories;

import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);
}
