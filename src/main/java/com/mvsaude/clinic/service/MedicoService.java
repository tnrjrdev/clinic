package com.mvsaude.clinic.service;


import com.mvsaude.clinic.dto.*;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Medico;
import com.mvsaude.clinic.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service @RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository repo;


    public List<MedicoDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList(); }


    public MedicoDTO obter(Long id) {
        return repo.findById(id).map(this::toDTO)
            .orElseThrow(() -> new BusinessException("Médico não encontrado")); }


    @Transactional
    public MedicoDTO criar(MedicoDTO dto) {
        repo.findByCrm(dto.crm()).ifPresent(m -> { throw new BusinessException("CRM já cadastrado"); });
        var m = Medico.builder().nome(dto.nome()).especialidade(dto.especialidade()).crm(dto.crm()).build();
        return toDTO(repo.save(m));
    }


    @Transactional
    public MedicoDTO atualizar(Long id, MedicoDTO dto) {
        var m = repo.findById(id).orElseThrow(() -> new BusinessException("Médico não encontrado"));
        m.setNome(dto.nome()); m.setEspecialidade(dto.especialidade()); m.setCrm(dto.crm());
        return toDTO(repo.save(m));
    }


    @Transactional
    public void remover(Long id) {
        repo.deleteById(id);
    }


    private MedicoDTO toDTO(Medico m) {
        return new MedicoDTO(m.getId(), m.getNome(), m.getEspecialidade(), m.getCrm()); }
}