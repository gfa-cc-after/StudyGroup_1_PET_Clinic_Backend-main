package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.dtos.delete.DeleteUserResponse;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.dtos.update.EditUserResponseDTO;
import com.greenfox.dramacsoport.petclinicbackend.errors.AppServiceErrors;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.UnauthorizedActionException;
import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.AppUserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
                "1234",
                "New_Name"
        );
        EditUserResponseDTO responseDTO = new EditUserResponseDTO("Changes saved.");

        when(appUserService.changeUserData(userEmail, requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        this.mockMvc.perform(patch("/api/v1/user/profile")
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
        this.mockMvc.perform(patch("/api/v1/user/profile")
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

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND));

        // Act & Assert
        MvcResult result = mockMvc.perform(patch("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);
        assertEquals("Please enter a valid email address!", errorResponse.get("email"));
        assertEquals("Please enter your original password!", errorResponse.get("originalPassword"));
        assertNull(errorResponse.get("password"));
        assertEquals("Please enter your display name!", errorResponse.get("displayName"));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - UNHAPPY PATH empty fields (HTTP BAD)")
    public void editUser_shouldThrowValidationErrorsWhenFieldsAreEmpty() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "",
                "",
                "",
                ""
        );

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND));

        // Act & Assert
        MvcResult result = mockMvc.perform(patch("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);
        assertEquals("Please enter a valid email address!", errorResponse.get("email"));
        assertEquals("Please enter your original password!", errorResponse.get("originalPassword"));
        assertEquals(AppServiceErrors.SHORT_PASSWORD, errorResponse.get("password"));
        assertEquals("Please enter your display name!", errorResponse.get("displayName"));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - UNHAPPY PATH blank fields (HTTP BAD)")
    public void editUser_shouldThrowValidationErrorsWhenFieldsAreBlank() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                " ",
                " ",
                " ",
                " "
        );

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND));

        // Act & Assert
        MvcResult result = mockMvc.perform(patch("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);

        assertThat(errorResponse.get("email"),
                either(Matchers.equalTo("Please enter a valid email address!"))
                        .or(Matchers.equalTo(AppServiceErrors.EMAIL_FIELD_NOT_VALID)));
        assertNull(errorResponse.get("originalPassword"));
        assertEquals(AppServiceErrors.SHORT_PASSWORD, errorResponse.get("password"));
        assertThat(errorResponse.get("displayName"),
                either(Matchers.equalTo("Please enter your display name!"))
                        .or(Matchers.equalTo("Display name can only contain alphanumeric characters")));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - UNHAPPY PATH length checks (HTTP BAD)")
    public void editUser_shouldThrowValidationErrorsWhenFieldLengthsNotValid() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "a@a.a",
                " ",
                "123",
                "Sooo-Loong-It-Wont-Fi"
        );

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND));

        // Act & Assert
        MvcResult result = mockMvc.perform(patch("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);
        assertNull(errorResponse.get("email"));
        assertNull(errorResponse.get("originalPassword"));
        assertEquals(AppServiceErrors.SHORT_PASSWORD, errorResponse.get("password"));
        assertEquals("Display name could not be longer than 20 characters", errorResponse.get("displayName"));
    }

    @Test
    @WithMockUser("testUser")
    @DisplayName("Edit user Controller - UNHAPPY PATH pattern checks (HTTP BAD)")
    public void editUser_shouldThrowValidationErrorsWhenFieldPatternIsNotValid() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "notanemailaddress",
                " ",
                "1234",
                "_Very-Sp3c1al:NAME"
        );

        when(appUserService.changeUserData(userEmail, requestDTO)).thenThrow(new UsernameNotFoundException(AppServiceErrors.USERNAME_NOT_FOUND));

        // Act & Assert
        MvcResult result = mockMvc.perform(patch("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isBadRequest())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        HashMap<String, String> errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                HashMap.class);
        assertEquals(AppServiceErrors.EMAIL_FIELD_NOT_VALID, errorResponse.get("email"));
        assertNull(errorResponse.get("originalPassword"));
        assertNull(errorResponse.get("password"));
        assertEquals("Display name can only contain alphanumeric characters", errorResponse.get("displayName"));
    }

    @Test
    @DisplayName("Edit user Controller - UNHAPPY PATH unauthorized user (HTTP Forbidden)")
    public void shouldRespondWithForbiddenWhenUnauthorizedUserTriesToUpdateUser() throws Exception {
        // Arrange
        String userEmail = "testUser";
        EditUserRequestDTO requestDTO = new EditUserRequestDTO(
                "new_email",
                "old_password",
                "new_password",
                "new_displayName"
        );
        EditUserResponseDTO responseDTO = new EditUserResponseDTO("Changes saved.");

        when(appUserService.changeUserData(userEmail, requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/v1/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(
                        status().isForbidden())
                .andReturn();

        verify(appUserService, never()).changeUserData(anyString(), any(EditUserRequestDTO.class));
        String errorResponse = result.getResponse().getErrorMessage();
        assertEquals("Access Denied", errorResponse);
    }
}