package com.greenfox.dramacsoport.petclinicbackend.services.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.DeletionErrorException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Pet;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void shouldNotAllowDeletionIfUserHasPets() {
        // Given
        String userEmail = "test@example.com";
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
        when(appUser.getPets()).thenReturn(List.of(new Pet()));
        when(appUser.getId()).thenReturn(1L);

        // When
        DeletionErrorException deletionErrorException = assertThrows(DeletionErrorException.class, () -> appUserService.deleteUser(userEmail, 1L));

        // Then
        assertEquals("Unable to delete your profile. Please transfer or delete your pets before proceeding.", deletionErrorException.getMessage());
        verify(appUserRepository, never()).delete(appUserCaptor.capture());
    }

    @Test
    public void shouldAllowDeletionIfUserHasNoPets() throws DeletionErrorException {
        // Given
        String userEmail = "test@example.com";
        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));
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
        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(appUser));
        when(appUser.getId()).thenReturn(1L);

        // When
        UnauthorizedActionException unauthorizedActionException = assertThrows(UnauthorizedActionException.class, () -> appUserService.deleteUser(userEmail, userId));

        // Then
        assertEquals("User is not authorized to delete this account", unauthorizedActionException.getMessage());
        verify(appUserRepository, never()).delete(appUserCaptor.capture());
    }
}
