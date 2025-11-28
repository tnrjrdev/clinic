package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.MedicoBloqueio;
import com.mvsaude.clinic.repository.MedicoBloqueioRepository;
import com.mvsaude.clinic.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos/{medicoId}/bloqueios")
@RequiredArgsConstructor
public class MedicoBloqueioController {

    private final MedicoRepository medicoRepo;
    private final MedicoBloqueioRepository repo;

    @PostMapping
    public MedicoBloqueio criar(@PathVariable Long medicoId, @RequestBody MedicoBloqueio dto) {
        var medico = medicoRepo.findById(medicoId)
                .orElseThrow(() -> new BusinessException("Médico não encontrado"));

        if (dto.getInicio() == null || dto.getFim().isBefore(dto.getInicio())) {
            throw new BusinessException("Período inválido");
        }

        dto.setId(null);
        dto.setMedico(medico);
        return repo.save(dto);
    }

    @GetMapping
    public List<MedicoBloqueio> listar(@PathVariable Long medicoId) {
        return repo.findByMedicoId(medicoId);
    }
}
