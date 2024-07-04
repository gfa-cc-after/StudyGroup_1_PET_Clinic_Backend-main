package com.greenfox.dramacsoport.petclinicbackend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/reg")
    public String register() {
        return "registration";
    }
}
