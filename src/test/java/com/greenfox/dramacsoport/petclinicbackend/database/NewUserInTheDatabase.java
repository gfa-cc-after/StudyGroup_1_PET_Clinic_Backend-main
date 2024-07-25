package com.greenfox.dramacsoport.petclinicbackend.database;

import com.greenfox.dramacsoport.petclinicbackend.dtos.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.models.MyUser;
import com.greenfox.dramacsoport.petclinicbackend.models.Role;
import com.greenfox.dramacsoport.petclinicbackend.repositories.MyUserRepository;
import com.greenfox.dramacsoport.petclinicbackend.services.MyUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class NewUserInTheDatabase {

    //which service we want to test
    @InjectMocks
    private MyUserService myUserService;


    //declare dependencies
    @Mock
    MyUserRepository myUserRepository;

    @Mock
    ModelMapper modelMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_save_a_user(){
        //given
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "My Name",
                "something@gmail.com",
                "password"
        );
        MyUser user = new MyUser(
                1L,
                "something@gmail.com",
                "My Name",
                "password",
                Role.USER
        );

        //Mock the calls
        when(myUserService.convertToEntity(dto))
                .thenReturn(user);

        //when
        MyUser myUser = myUserService.registerUser(dto);

        //Then
        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getRole(), user.getRole());
    }

}
