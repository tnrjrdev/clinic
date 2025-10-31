package com.mvsaude.clinic.service;

import com.mvsaude.clinic.dto.PacienteDTO;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Paciente;
import com.mvsaude.clinic.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository repo;

    public List<PacienteDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public PacienteDTO obter(Long id) {
        return repo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new BusinessException("Paciente não encontrado"));
    }

    @Transactional
    public PacienteDTO criar(PacienteDTO dto) {
        repo.findByCpf(dto.cpf()).ifPresent(p -> {throw  new BusinessException("CPF já cadastrado"); });
        var p = Paciente.builder().nome(dto.nome()).cpf(dto.cpf()).dataNascimento(dto.dataNascimento()).build();
        return toDTO(repo.save(p));
    }

    @Transactional
    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        var p = repo.findById(id).orElseThrow(() -> new BusinessException("Paciente não encontrado"));
        p.setNome(dto.nome()); p.setCpf(dto.cpf());p.setDataNascimento(dto.dataNascimento());
        return toDTO(repo.save(p));
    }

    @Transactional
    public void remover(Long id) {
        repo.deleteById(id);
    }

    private PacienteDTO toDTO(Paciente p) {
        return new PacienteDTO(p.getId(), p.getNome(), p.getCpf(), p.getDataNascimento());
    }

}
