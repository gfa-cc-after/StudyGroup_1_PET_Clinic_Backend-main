package com.greenfox.dramacsoport.petclinicbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {

    @Value("${cors.urls}")
    public String corsUrls;
}
