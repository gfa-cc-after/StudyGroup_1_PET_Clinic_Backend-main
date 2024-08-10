package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.services.AppUserDetailsService;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    //Services to test
    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    //mock dependencies
    @Mock
    JwtService jwtService;

    @Mock
    AppUserDetailsService appUserDetailsService;

    @Mock
    FilterChain filterChain;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    /**
     * <h2>Authorization on HAPPY PATH</h2>
     * <h3>When the user is not yet authorized AND has a valid token</h3>
     * <ul>
     *     <li>Should authorize user based on the token</li>
     *     <li>Should update the SecurityContextHolder to authorized</li>
     * </ul>
     */
    @Test
    @DisplayName("Happy path (not authorized, valid header and token)")
    public void shouldAuthorizeWhenAuthHeaderAndTokenIsValid() throws ServletException, IOException {

        //GIVEN
        Authentication SecurityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(SecurityContextAuthBefore);
        UserDetails userDetails = User.builder()
                .username("user")
                .password("password")
                .roles(Role.USER.name())
                .build();

        String token = "token";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(appUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.extractUsername(anyString())).thenReturn("user");
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain, times(1)).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNotNull(securityContextAuthNow);
        Assertions.assertTrue(securityContextAuthNow.isAuthenticated());
    }

    //UNHAPPY PATH

    /**
     * <h3>Missing Authorization Header</h3>
     * Doesn't update SecurityContext when AuthorizationHeader is missing
     */
    @Test
    @DisplayName("AuthorizationHeader is missing")
    public void shouldNotUpdateSecurityContextWhenAuthHeaderIsMissing() throws ServletException, IOException {
        //GIVEN
        Authentication securityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(securityContextAuthBefore);

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain, times(1)).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(securityContextAuthNow);
    }

    /**
     * <h3>Authorization Header does not begin with Bearer</h3>
     * Doesn't update SecurityContext when AuthorizationHeader does not begin with "Bearer "
     */
    @Test
    @DisplayName("Does not begin with Bearer ")
    public void shouldNotUpdateSecurityContextWhenAuthHeaderIsInvalid() throws ServletException, IOException {
        //GIVEN
        Authentication SecurityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(SecurityContextAuthBefore);

        String token = "Invalid_TOKEN";
        request.addHeader("Authorization", token);

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain, times(1)).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertNull(securityContextAuthNow);
    }

    /**
     * <h3>Invalid username from token</h3>
     * Doesn't update SecurityContext when username cannot be retrieved from token or user could not be found in
     * database"
     */
    @Test
    @DisplayName("Invalid username from token")
    public void shouldNotUpdateSecurityContextWhenUsernameIsNotValid() throws ServletException, IOException {
    }

    /**
     * <h3>Invalid token (expired)</h3>
     * Doesn't update SecurityContext when token expired"
     */
    @Test
    @DisplayName("Invalid token (expired)")
    public void shouldNotUpdateSecurityContextWhenTokenExpired() throws ServletException, IOException {
    }

    /**
     * <h3>User already authenticated in SecurityContextHolder</h3>
     * Doesn't update not null authenticated SecurityContext"
     */
    @Test
    @DisplayName("Already authenticated")
    public void shouldNotUpdateSecurityContextWhenUserAlreadyAuthenticated() throws ServletException, IOException {
    }
}