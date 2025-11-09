package com.mvsaude.clinic.auth;

import com.mvsaude.clinic.model.Role;
import com.mvsaude.clinic.model.Usuario;
import com.mvsaude.clinic.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component @RequiredArgsConstructor
public class UserSeeder {
    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @PostConstruct
    public void seed() {
        if (repo.findByUsername("admin").isEmpty()) {
            repo.save(Usuario.builder().username("admin").password(encoder.encode("admin"))
                    .role(Role.ROLE_ADMIN).build());
        }
        if (repo.findByUsername("user").isEmpty()) {
            repo.save(Usuario.builder().username("user").password(encoder.encode("user"))
                    .role(Role.ROLE_USER).build());
        }
    }
}