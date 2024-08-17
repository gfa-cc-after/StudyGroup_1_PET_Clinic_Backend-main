package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@ComponentScan(value = "com.greenfox.dramacsoport.petclinicbackend")
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserService appUserService;

    public JwtAuthenticationFilter(JwtService jwtService, @Lazy AppUserService appUserService) {
        this.jwtService = jwtService;
        this.appUserService = appUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Check the header for the Authorization Bearer token
        String bearer = "Bearer "; // this is just a prefix, the actual token starts from the 7th character
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(bearer)) {
            filterChain.doFilter(request, response);
            return;
        }

        //Remove the Bearer prefix, validate the token and check if the user is authenticated
        String jwt = authHeader.substring(bearer.length());
        if (!jwtService.isTokenValid(jwt) || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the username from the token and load the user details
        String username = jwtService.extractUsername(jwt);
        UserDetails userDetails = appUserService.loadUserByUsername(username);

        // Set user details into the security context, and authenticate the user
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}