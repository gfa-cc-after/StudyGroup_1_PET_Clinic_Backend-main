package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.IncorrectPasswordException;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppUserControllerTest {

    @MockBean
    private AppUserService appUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser("testuser")
    @DisplayName("Delete user Controller - HAPPY PATH (HTTP 200 OK)")
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
    @DisplayName("Delete user Controller - UNHAPPY PATH (HTTP 403 FORBIDDEN)")
    public void deleteUser_shouldReturn403Forbidden_whenUserIsAuthenticatedAndIdIsNotMatching() throws Exception {
        // Arrange
        when(appUserService.deleteUser("testuser@test.com", 2L)).thenThrow(new UnauthorizedActionException("User is " +
                "not authorized to delete this account"));

        // Act & Assert
        this.mockMvc.perform(delete("/api/v1/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - HAPPY PATH (HTTP 200 OK)")
    public void editUser_shouldReturn200Ok_whenUserIsAuthenticatedAndDataIsCorrect() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "new@example.com",
                "0ld_P4ssw0rd",
                "n3w-pAss_word",
                "New_Name"
        );
        EditUserResponseDTO responseDTO = new EditUserResponseDTO("Changes saved.");

        when(appUserService.changeUserData(userEmail, requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpectAll(
                        status().isOk(),
                        content().json("{\"message\":\"Changes saved.\"}")
                );
        verify(appUserService).changeUserData(anyString(), any(EditUserRequestDTO.class));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - HAPPY PATH with PW null (HTTP 200 OK)")
    public void editUser_shouldReturn200Ok_whenUserIsAuthenticatedAndDataIsCorrectNullPW() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "new@example.com",
                "0ld_P4ssw0rd",
                null,
                "New_Name"
        );
        EditUserResponseDTO responseDTO = new EditUserResponseDTO("Changes saved.");

        when(appUserService.changeUserData(userEmail, requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        this.mockMvc.perform(post("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpectAll(
                        status().isOk(),
                        content().json("{\"message\":\"Changes saved.\"}")
                );
        verify(appUserService).changeUserData(anyString(), any(EditUserRequestDTO.class));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - UNHAPPY PATH null fields (HTTP BAD)")
    public void editUser_shouldThrowValidationErrorsWhenFieldsAreNull() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                null,
                null,
                null,
                null
        );

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new IncorrectPasswordException());

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);
        Assertions.assertEquals("Please enter a valid email address!", errorResponse.get("email"));
        Assertions.assertEquals("Please enter your original password!", errorResponse.get("originalPassword"));
        Assertions.assertNull(errorResponse.get("password"));
        Assertions.assertEquals("Please enter your display name!", errorResponse.get("displayName"));


    }
}