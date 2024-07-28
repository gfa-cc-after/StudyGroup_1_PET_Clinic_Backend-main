package com.greenfox.dramacsoport.petclinicbackend.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String username;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
