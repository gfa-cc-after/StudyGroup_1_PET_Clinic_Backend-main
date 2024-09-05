package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.JwtService;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.NameAlreadyBoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUser appUser;

    @Captor
    private ArgumentCaptor<AppUser> appUserCaptor;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthService authService;

    @Test
    public void shouldNotAllowDeletionIfUserHasPets() {
        // Given
        String userEmail = "test@example.com";
        when(appUserRepository.findByEmail(anyString())).thenReturn(appUser);
        when(appUser.getPets()).thenReturn(List.of(new Pet()));
        when(appUser.getId()).thenReturn(1L);

        // When
        DeletionException deletionException = assertThrows(DeletionException.class,
                () -> appUserService.deleteUser(userEmail, 1L));

        // Then
        assertEquals("Unable to delete your profile. Please transfer or delete your pets before proceeding.",
                deletionException.getMessage());
        verify(appUserRepository, never()).delete(appUserCaptor.capture());
    }

    @Test
    public void shouldAllowDeletionIfUserHasNoPets() throws DeletionException {
        // Given
        String userEmail = "test@example.com";
        when(appUserRepository.findByEmail(anyString())).thenReturn(appUser);
        when(appUser.getPets()).thenReturn(List.of());
        when(appUser.getId()).thenReturn(1L);

        // When
        DeleteUserResponse deleteUserResponse = appUserService.deleteUser(userEmail, 1L);

        // Then
        assertEquals("Your profile has been successfully deleted.", deleteUserResponse.message());
        verify(appUserRepository).delete(appUserCaptor.capture());
        assertEquals(appUser, appUserCaptor.getValue());
    }

    @Test
    public void shouldThrowExceptionWhenUserIdDoesNotMatchEmailId() {
        // Given
        String userEmail = "test@example.com";
        Long userId = 2L;
        when(appUserRepository.findByEmail("test@example.com")).thenReturn(appUser);
        when(appUser.getId()).thenReturn(1L);

        // When
        UnauthorizedActionException unauthorizedActionException = assertThrows(UnauthorizedActionException.class,
                () -> appUserService.deleteUser(userEmail, userId));

        // Then
        assertEquals("User is not authorized to delete this account", unauthorizedActionException.getMessage());
        verify(appUserRepository, never()).delete(appUserCaptor.capture());
    }

    @Test
    @DisplayName("Update user - HAPPY PATH")
    public void changeUserDataMethodIsSuccessfullyCalled() throws NameAlreadyBoundException {
        //Arrange: Mock user from token and mock request DTO
        EditUserRequestDTO request = new EditUserRequestDTO(
                "newEmail@example.com",
                "Pr3v_p4ssw0rd",
                "N3w_p4ssw0rd",
                "Edited_N3w-N4me");

        AppUser dbUser = AppUser.builder()
                .id(1L)
                .displayName("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .pets(List.of())
                .build();

        //old user mock
        AppUser oldUser = new AppUser();
        oldUser.setId(dbUser.getId());
        oldUser.setEmail(dbUser.getEmail());
        oldUser.setPassword(dbUser.getPassword());
        oldUser.setDisplayName(dbUser.getDisplayName());
        oldUser.setRole(dbUser.getRole());

        //Mock methods
        when(appUserRepository.findByEmail(anyString())).thenReturn(dbUser);
        when(appUserRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPW");
        when(passwordEncoder.matches(request.originalPassword(), dbUser.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(request.password(), dbUser.getPassword())).thenReturn(false);
        when(authService.isPasswordLongerThanThreeChar(anyString())).thenReturn(true);

        //Call method
        appUserService.changeUserData(dbUser.getEmail(), request);

        //Check if every method had been called
        verify(appUserRepository).findByEmail(oldUser.getEmail());
        verify(appUserRepository).existsByEmail(request.email());
        verify(passwordEncoder).matches(request.originalPassword(), oldUser.getPassword());
        verify(passwordEncoder).matches(request.password(), oldUser.getPassword());
        verify(passwordEncoder).encode(request.password());
        verify(appUserRepository).save(appUserCaptor.capture());
        verify(jwtService).logoutUser();

        assertNotEquals(dbUser, oldUser);
        assertEquals(dbUser.getId(), oldUser.getId());
        assertEquals(dbUser.getRole(), oldUser.getRole());
        assertEquals(dbUser.getPets(), oldUser.getPets());
        assertEquals(request.email(), appUserCaptor.getValue().getEmail());
        assertEquals(passwordEncoder.encode(request.password()), appUserCaptor.getValue().getPassword());
        assertEquals(request.displayName(), appUserCaptor.getValue().getDisplayName());
    }
}
