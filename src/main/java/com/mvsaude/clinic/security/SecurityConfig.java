package com.mvsaude.clinic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // SecurityConfig.java
    @Bean
    public SecurityFilterChain security(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.disable()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

                        // Consultas: permitir PATCH de status
                        .requestMatchers(HttpMethod.PATCH, "/api/consultas/*/status").authenticated()

                        // Pagamentos
                        .requestMatchers(HttpMethod.POST, "/api/pagamentos/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/*/recibo").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/**").authenticated()

                        // Demais: autenticado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
