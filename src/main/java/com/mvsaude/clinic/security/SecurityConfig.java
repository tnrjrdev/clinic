package com.mvsaude.clinic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // para @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain security(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.disable()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // público
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

                        // swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()

                        // pacientes: listar/obter → ADMIN ou USER; criar/alterar/remover → ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/**")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/pacientes/**")
                        .hasRole("ADMIN")

                        // médicos: só ADMIN
                        .requestMatchers("/api/medicos/**").hasRole("ADMIN")

                        // consultas: leitura para ADMIN/USER, alteração só ADMIN ou futuro ROLE_RECEPCAO
                        .requestMatchers(HttpMethod.GET, "/api/consultas/**")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/consultas/**")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/consultas/**")
                        .hasAnyRole("ADMIN", "USER")

                        // pagamentos – financeiro/admin por enquanto
                        .requestMatchers("/api/pagamentos/**")
                        .hasAnyRole("ADMIN", "USER")

                        // prontuário – por enquanto só ADMIN e USER (depois você separa ROLE_MEDICO)
                        .requestMatchers("/api/pacientes/*/prontuario/**")
                        .hasAnyRole("ADMIN", "USER")

                        // dashboards – recepção/médicos/admin (por enquanto ADMIN/USER)
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole("ADMIN", "USER")

                        // recibos
                        .requestMatchers("/api/recibos/**")
                        .hasAnyRole("ADMIN", "USER")

                        // tudo o resto: autenticado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
