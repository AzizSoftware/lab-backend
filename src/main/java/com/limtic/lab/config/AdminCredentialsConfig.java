package com.limtic.lab.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AdminCredentialsConfig {

    // Predefined admin email/password (hashed)
    @Value("${admin.email:admin@lab.com}")
    private String email;

    @Value("${admin.password:$2a$10$EIX...}") // bcrypt hashed
    private String password;

    @Value("${admin.role:SUPER_ADMIN}")
    private String role;
}
