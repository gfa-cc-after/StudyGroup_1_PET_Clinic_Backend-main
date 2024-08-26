package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppUserControllerTest {

    @MockBean
    private AppUserService appUserService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("testuser")
    public void deleteUser_shouldReturn200Ok_whenUserIsAuthenticatedAndIdIsMatchingAndSuccessfullyDeleted() throws Exception {
        // Arrange
        DeleteUserResponse response = new DeleteUserResponse("Your profile has been successfully deleted.");

        when(appUserService.deleteUser(anyString(), anyLong())).thenReturn(response);

        // Act
        this.mockMvc.perform(delete("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Your profile has been successfully deleted.\"}"));

        // Assert
        verify(appUserService, times(1)).deleteUser("testuser", 1L);
    }
    
    @Test
    @WithMockUser("testuser@test.com")
    public void deleteUser_shouldReturn403Forbidden_whenUserIsAuthenticatedAndIdIsNotMatching() throws Exception {
        // Arrange
        when(appUserService.deleteUser("testuser@test.com", 2L)).thenThrow(new UnauthorizedActionException("User is not authorized to delete this account"));

        // Act & Assert
        this.mockMvc.perform(delete("/api/v1/user/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
    }
}