package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.exceptions.ValidationException;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NameAlreadyBoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class RegisterController {

    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO newUserDTO,
                                          BindingResult bindingResult) throws NameAlreadyBoundException {
        if (bindingResult.hasErrors()) {
            logger.error("errors during validation {}", bindingResult.getAllErrors());
            throw new ValidationException("Validation errors: " + bindingResult.getAllErrors());
        }

        authService.registerUser(newUserDTO);
        logger.info("successful reg");
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }
}

