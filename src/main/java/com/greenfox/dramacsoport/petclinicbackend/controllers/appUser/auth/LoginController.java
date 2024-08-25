package com.greenfox.dramacsoport.petclinicbackend.controllers.appUser.auth;

import com.greenfox.dramacsoport.petclinicbackend.dtos.login.LoginRequestDTO;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class LoginController {

    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO requestDTO,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        logger.info("Login request successful: {}", requestDTO);
        return new ResponseEntity<>(authService.login(requestDTO), HttpStatus.OK);
    }
}