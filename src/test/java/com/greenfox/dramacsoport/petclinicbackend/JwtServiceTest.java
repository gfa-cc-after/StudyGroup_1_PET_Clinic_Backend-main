package com.greenfox.dramacsoport.petclinicbackend;

import com.greenfox.dramacsoport.petclinicbackend.config.webtoken.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtServiceTest {

    @MockBean
    private JwtService jwtService;

    @Test
    public void testJwtTokenGeneration() {
        UserDetails userDetails = new User(
                "testUser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(jwtService.generateToken(userDetails)).thenReturn("mockToken");

        String token = jwtService.generateToken(userDetails);

        when(jwtService.extractUsername(token)).thenReturn(userDetails.getUsername());

        String username = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), username);
    }
}