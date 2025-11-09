package com.mvsaude.clinic.repository;

import com.mvsaude.clinic.model.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    Optional<Prontuario> findByPacienteId(Long paciendeId);
}
