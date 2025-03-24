package com.example.addiction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Matikan CSRF agar bisa test dengan Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Mengizinkan semua request tanpa autentikasi
                );
        return http.build();
    }
}
