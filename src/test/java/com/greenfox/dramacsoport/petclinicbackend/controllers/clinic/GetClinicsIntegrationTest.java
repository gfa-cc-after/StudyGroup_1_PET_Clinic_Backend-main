package com.greenfox.dramacsoport.petclinicbackend.controllers.clinic;

import com.greenfox.dramacsoport.petclinicbackend.models.AppUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Clinic;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.AppUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.repositories.ClinicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GetClinicsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    public void setUp() {
        AppUser user = new AppUser();
        user.setEmail("user@example.com");
        user.setPassword("Password");
        AppUser admin = new AppUser();
        admin.setEmail("admin@example.com");
        admin.setPassword("Password");
        admin.setRole(Role.ADMIN);

        appUserRepository.saveAll(Arrays.asList(user, admin));

        Clinic clinic1 = new Clinic();
        clinic1.setName("Clinic 1");
        clinic1.setAddress("Test Address 1");
        Clinic clinic2 = new Clinic();
        clinic2.setName("Clinic 2");
        clinic2.setAddress("Test Address 2");

        clinicRepository.saveAll(Arrays.asList(clinic1,clinic2));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testIfWholeClinicListSentToUserWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/v1/admin/clinics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clinics[0].name").value("Clinic 1"))
                .andExpect(jsonPath("$.clinics[1].name").value("Clinic 2"))
                .andExpect(jsonPath("$.clinics.length()").value(clinicRepository.count()));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void testIfForbiddenResponseSentToUserWithUserRole() throws Exception {
        mockMvc.perform(get("/api/v1/admin/clinics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
