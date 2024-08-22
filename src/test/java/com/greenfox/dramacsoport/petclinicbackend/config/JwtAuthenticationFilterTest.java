package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    AppUserRepository repository;

    @Mock
    FilterChain filterChain;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
        appUser = AppUser.builder()
                .email("user@example.com")
                .displayName("user")
                .password("password")
                .role(Role.USER)
                .build();
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
        assertNull(SecurityContextAuthBefore);

        String token = "token";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn(appUser.getUsername());
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain).doFilter(request, response);
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
        assertNull(securityContextAuthBefore);

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        assertNull(securityContextAuthNow);
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
        assertNull(SecurityContextAuthBefore);

        String token = "Invalid_TOKEN";
        request.addHeader("Authorization", token);

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        assertNull(securityContextAuthNow);
    }

    /**
     * <h3>Invalid token (expired)</h3>
     * Doesn't update SecurityContext when token expired"
     */
    @Test
    @DisplayName("Invalid token (expired)")
    public void shouldNotUpdateSecurityContextWhenTokenExpired() throws ServletException, IOException {
        //GIVEN
        Authentication SecurityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        assertNull(SecurityContextAuthBefore);

        String token = "EXPIRED_TOKEN";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(jwtService.isTokenValid(anyString())).thenReturn(false);

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(filterChain).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        assertNull(securityContextAuthNow);
    }

    /**
     * <h3>User already authenticated in SecurityContextHolder</h3>
     * Doesn't update not null authenticated SecurityContext"
     */
    @Test
    @DisplayName("Already authenticated")
    public void shouldNotUpdateSecurityContextWhenUserAlreadyAuthenticated() throws ServletException, IOException {

        //GIVEN
        String token = "AUTHENTICATED_TOKEN";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(jwtService.isTokenValid(anyString())).thenReturn(true);

        // Simulate a non-null authentication object in the SecurityContextHolder
        UsernamePasswordAuthenticationToken firstAuthentication = new UsernamePasswordAuthenticationToken(
                appUser.getUsername(),
                appUser.getPassword(),
                appUser.getAuthorities()
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        securityContext.setAuthentication(firstAuthentication);
        when(securityContext.getAuthentication())
                .thenReturn(firstAuthentication);
        SecurityContextHolder.setContext(securityContext);
        Assertions.assertNotNull(securityContext);
        Assertions.assertTrue(securityContext.getAuthentication().isAuthenticated());

        //WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        //THEN
        verify(jwtService, never()).extractUsername(anyString());
        verify(repository, never()).findByEmail(anyString());
        verify(securityContext, times(1)).setAuthentication(any());
        verify(filterChain).doFilter(request, response);

        securityContext = SecurityContextHolder.getContext();
        Assertions.assertTrue(securityContext.getAuthentication().isAuthenticated());
    }

    /**
     * <h3>Missing username field in token</h3>
     * Doesn't update SecurityContext when username cannot be retrieved from token
     */
    @Test
    @DisplayName("Missing username field in token")
    public void shouldNotUpdateSecurityContextWhenUsernameCannotBeExtractedFromToken() throws ServletException,
            IOException {
        //GIVEN
        Authentication SecurityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        assertNull(SecurityContextAuthBefore);

        String token = "MISSING_USERNAME_TOKEN";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn(null);
        when(repository.findByEmail(null))
                .thenThrow(new UsernameNotFoundException("Bad credentials"));

        //ACT and ASSERT
        assertThrows(UsernameNotFoundException.class, () ->
                jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        //THEN
        verify(filterChain, never()).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        assertNull(securityContextAuthNow);
    }

    /**
     * <h3>Invalid username in token</h3>
     * Doesn't update SecurityContext when username does not match any user in database
     */
    @Test
    @DisplayName("Invalid username in token")
    public void shouldNotUpdateSecurityContextWhenUsernameCannotBeFoundInDatabase() throws ServletException,
            IOException {
        //GIVEN
        Authentication SecurityContextAuthBefore = SecurityContextHolder.getContext().getAuthentication();
        assertNull(SecurityContextAuthBefore);

        String token = "INVALID_USERNAME_TOKEN";
        request.addHeader("Authorization", "Bearer " + token);

        //MOCK CALLS
        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn(appUser.getUsername());
        when(repository.findByEmail(anyString())).thenThrow(new UsernameNotFoundException("Bad " +
                "credentials"));

        //ACT and ASSERT
        assertThrows(UsernameNotFoundException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        //THEN
        verify(filterChain, never()).doFilter(request, response);
        Authentication securityContextAuthNow = SecurityContextHolder.getContext().getAuthentication();
        assertNull(securityContextAuthNow);
    }

}