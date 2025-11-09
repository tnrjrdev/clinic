package com.mvsaude.clinic.service;

import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Prontuario;
import com.mvsaude.clinic.repository.PacienteRepository;
import com.mvsaude.clinic.repository.ProntuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository repo;
    private final PacienteRepository pacienteRepo;

    @Transactional
    public Prontuario salvar(Long pacienteId, String texto) {
        var paciente = pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new BusinessException("Paciente não encotrado"));

        var prontuario = repo.findByPacienteId(pacienteId)
                .orElse(Prontuario.builder().paciente(paciente).build());

        prontuario.setAnotacoes(texto);
        return repo.save(prontuario);
    }

    public Prontuario buscar(Long pacienteId) {
        return repo.findByPacienteId(pacienteId)
                .orElseThrow(() -> new BusinessException("Prontuário não encotrado para o paciente "));
    }
}
