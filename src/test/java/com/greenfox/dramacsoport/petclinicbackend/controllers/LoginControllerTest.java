package com.greenfox.dramacsoport.petclinicbackend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfox.dramacsoport.petclinicbackend.services.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AppUserService appUserService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void responseShouldBeForbiddenIfUserNotFound(){

    }

    @Test
    public void responseShouldBeBadCredentialsIfErrorWhileLogin(){

    }

    @Test
    public void responseShouldBeOkIfCorrectCredentialsProvided(){

    }

    @Test
    public void tokenShouldBeSentIfCorrectCredentialsProvided(){

    }

}
