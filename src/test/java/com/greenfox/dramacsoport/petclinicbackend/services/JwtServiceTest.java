package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void shouldCheckIfTokenIsValid() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles(Role.USER.toString())
                .build();

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    public void shouldGetUsername() {
        UserDetails userDetails = User.builder()
                .username("testUser")
                .password("password")
                .roles(Role.USER.toString())
                .build();

        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void shouldGetValidRoles() {
        for (Role role : Role.values()) {
            UserDetails testUser = User.builder()
                    .username("testUser")
                    .password("password")
                    .roles(role.toString())
                    .build();

            String token = jwtService.generateToken(testUser);

            Role extractedRole = jwtService.extractRole(token);
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + extractedRole.name());
            Set<GrantedAuthority> authorities = Collections.singleton(authority);

            assertEquals(testUser.getAuthorities(), authorities);
        }
    }

    @Test
    public void shouldEncodeLowercaseRoles() {
        for (Role role : Role.values()) {
            UserDetails testUser = User.builder()
                    .username("testUser")
                    .password("password")
                    .roles(role.toString())
                    .build();

            String token = jwtService.generateToken(testUser);
            String extractedRole = jwtService.getClaims(token).get("role", String.class);

            String lowercaseRole = role.toString().toLowerCase();

            assertEquals(lowercaseRole, extractedRole);
        }
    }

    @Test
    public void shouldGetInvalidRole() {
        UserDetails testUser = User.builder()
                .username("testUser")
                .password("password")
                .roles("Invalid_ROLE")
                .build();

        String token = jwtService.generateToken(testUser);

        assertThrows(IllegalArgumentException.class, () -> jwtService.extractRole(token));
    }
}