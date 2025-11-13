package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.ConsultaDTO;
import com.mvsaude.clinic.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ConsultaService consultaService;

    @GetMapping("/recepcao/consultas-hoje")
    public List<ConsultaDTO> consultasHojeRecepcao() {
        return consultaService.consultasHojeRecepcao();
    }

    @GetMapping("/medicos/{medicoId}/consultas-hoje")
    public List<ConsultaDTO> consultasHojeMedico(@PathVariable Long medicoId) {
        return consultaService.consultaHojeMedico(medicoId);
    }
}
