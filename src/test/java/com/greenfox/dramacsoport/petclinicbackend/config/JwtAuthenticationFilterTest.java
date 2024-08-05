package com.greenfox.dramacsoport.petclinicbackend.config;

import com.greenfox.dramacsoport.petclinicbackend.services.AppUserDetailsService;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthenticationFilterTest {

    //Services to test
    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    JwtService jwtService;

    //mock dependencies
    @Mock
    AppUserDetailsService appUserDetailsService;

    @Mock
    FilterChain filterChain;

    MockHttpServletRequest request;

    MockHttpServletResponse response;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void shouldAuthorizeWhenAuthHeaderAndTokenIsValid() {


    }

    //UNHAPPY PATH

    /**
     * <h3>Missing Authorization Header</h3>
     * Doesn't update SecurityContext when AuthorizationHeader is missing
     */
    @Test
    @DisplayName("AuthorizationHeader is missing")
    public void shouldNotUpdateSecurityContextWhenAuthHeaderIsMissing() {
    }

    /**
     * <h3>Authorization Header does not begin with Bearer</h3>
     * Doesn't update SecurityContext when AuthorizationHeader does not begin with "Bearer "
     */
    @Test
    @DisplayName("Does not begin with Bearer ")
    public void shouldNotUpdateSecurityContextWhenAuthHeaderIsInvalid() {
    }

    /**
     * <h3>Invalid username from token</h3>
     * Doesn't update SecurityContext when username cannot be retrieved from token or user could not be found in
     * database"
     */
    @Test
    @DisplayName("Invalid username from token")
    public void shouldNotUpdateSecurityContextWhenUsernameIsNotValid() {
    }

    /**
     * <h3>Invalid token (expired)</h3>
     * Doesn't update SecurityContext when token expired"
     */
    @Test
    @DisplayName("Invalid token (expired)")
    public void shouldNotUpdateSecurityContextWhenTokenExpired() {
    }

    /**
     * <h3>User already authenticated in SecurityContextHolder</h3>
     * Doesn't update not null authenticated SecurityContext"
     */
    @Test
    @DisplayName("Already authenticated")
    public void shouldNotUpdateSecurityContextWhenUserAlreadyAuthenticated() {
    }
}