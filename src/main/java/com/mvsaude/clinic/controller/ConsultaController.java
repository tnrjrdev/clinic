package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.ConsultaDTO;
import com.mvsaude.clinic.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/paciente/{pacienteId}/historico")
    public List<ConsultaDTO> historicoPaciente(@PathVariable Long pacienteId) {
        return service.historicoPaciente(pacienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaDTO agendar(@RequestBody @Valid ConsultaDTO dto) {
        return service.agendar(dto);
    }

    @PatchMapping("/{id}/iniciar")
    public  ConsultaDTO iniciar(@PathVariable Long id) {
        return service.iniciarAtendimento(id);
    }

    @PatchMapping("/{id}/concluir")
    public ConsultaDTO concluir(@PathVariable Long id) {
        return service.concluir(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ConsultaDTO> alterarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        var novoStatus = body.get("status"); // "EM_ATENDIMENTO", "CONCLUIDA"...
        return ResponseEntity.ok(service.alterarStatus(id, novoStatus));
    }
}
