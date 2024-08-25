package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.register.RegisterRequestDTO;
import com.greenfox.dramacsoport.petclinicbackend.services.appUser.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class RegisterController {

    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO newUserDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("errors during validation {}", bindingResult.getAllErrors());
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        authService.registerUser(newUserDTO);
        logger.info("successful reg");
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }
}

