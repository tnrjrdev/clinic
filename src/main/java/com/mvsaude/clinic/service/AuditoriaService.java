package com.mvsaude.clinic.service;

import com.mvsaude.clinic.model.Auditoria;
import com.mvsaude.clinic.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository repo;

    public void registrar(String entidade, String acao, String detalhes) {
        String usuario = "anon";
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            usuario = auth.getName();
        }

        var a = Auditoria.builder()
                .entidade(entidade)
                .acao(acao)
                .detalhes(detalhes)
                .usuario(usuario)
                .dataHora(LocalDateTime.now())
                .build();

        repo.save(a);
    }
}
