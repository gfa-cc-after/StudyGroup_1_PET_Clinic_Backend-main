package com.greenfox.dramacsoport.petclinicbackend.services;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private final String secretKey = secretKeyGenerator();
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    /**
     * <h2>Creates a JWT token from a UserDetails object.</h2>
     * By default, the user roles are stored as a GrantedAuthority with a ROLE_ prefix in the UserDetails object (e.g
     * . ROLE_USER).
     * To be stored in the token, it has to be mapped with the Role.getRole() method first.
     * For easier handling on the frontend, the role is stored as lowercase String in the token.
     * @param user to use for creating a JWT token
     * @return a valid JWT token
     */
    public String generateToken(AppUser user) {
        Map<String, String> claims = new HashMap<>();
        GrantedAuthority firstAuthority = user.getAuthorities().iterator().next();
        String roleNameAsString = Role.getRole(firstAuthority).toString();

        String roleAsLowercaseString = roleNameAsString.toLowerCase();
        claims.put("role", roleAsLowercaseString);
        claims.put("displayName", user.getDisplayName());
        claims.put("email", user.getUsername());
        claims.put("id", String.valueOf(user.getId()));
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {     //jwt is the token without prefix
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public Role extractRole(String jwt) {
        Claims claims = getClaims(jwt);
        String roleFromToken = claims.get("role", String.class);
        String roleAsString = roleFromToken.toUpperCase();
        return Role.fromString(roleAsString);
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private String secretKeyGenerator() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public boolean logoutUser() {
        //TODO: write unit test for this method
        SecurityContextHolder.getContext().setAuthentication(null);
        return true;
    }

}
