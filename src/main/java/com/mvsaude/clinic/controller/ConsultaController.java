package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.ConsultaDTO;
import com.mvsaude.clinic.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {
    private final ConsultaService service;

    @GetMapping
    public List<ConsultaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/medico/{medicoId}/futuras")
    public List<ConsultaDTO> futurasPorMedico(@PathVariable Long medicoId) {
        return service.futurasPorMedico(medicoId);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<ConsultaDTO> porPaciente(@PathVariable Long pacienteId) {
        return service.porPaciente(pacienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaDTO agendar(@RequestBody @Valid ConsultaDTO dto) {
        return service.agendar(dto);
    }
}
