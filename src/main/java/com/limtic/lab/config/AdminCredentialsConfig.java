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

    @Value("${admin.password:1234}") // bcrypt hashed
    private String password;

    @Value("${admin.role:SUPER_ADMIN}")
    private String role;
}
