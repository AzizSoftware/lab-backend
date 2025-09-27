package com.limtic.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ---------------- PUBLIC ----------------
                        .requestMatchers(HttpMethod.POST, "/api/users/signup", "/api/users/login").permitAll()

                        // ---------------- USERS ----------------
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{email}")
                        .hasAnyRole("User", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{email}")
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/users/{email}/photo")
                        
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/{email}/uploads").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/uploads/**")
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{email}/role")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // ---------------- PROJECTS ----------------
                        .requestMatchers(HttpMethod.GET, "/api/projects/**")
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/projects")
                        .hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/{id}", "/api/projects/{id}/addMember/**")
                        .hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/{id}")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // ---------------- FILES ----------------
                        .requestMatchers(HttpMethod.GET, "/api/files/**")
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/files").hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/files/{id}")
                        .hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/files/{id}")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // ---------------- EVENTS ----------------
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                        //
                        .requestMatchers(HttpMethod.POST, "/api/events").permitAll()
                        
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}").permitAll()
                        //.hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/{id}").permitAll()
                        //.hasAnyRole("PERMANENT", "ADMIN", "SUPER_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/{id}/enroll/**")
                        .hasAnyRole("USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")

                        // ---------------- DASHBOARD ----------------
                        .requestMatchers(HttpMethod.GET, "/api/dashboard")
                        .hasAnyRole("VISITOR", "USER", "PERMANENT", "ADMIN", "SUPER_ADMIN")

                        // ---------------- ADMIN ONLY ----------------
                        .requestMatchers("/api/admin/**").permitAll()

                        
                       .anyRequest().permitAll() //.authenticated()
                        )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
