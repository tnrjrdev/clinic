package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.model.Prontuario;
import com.mvsaude.clinic.service.ProntuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes/{pacienteId}/prontuario")
@RequiredArgsConstructor
public class ProntuarioController {

    private final ProntuarioService service;

    @GetMapping
    public Prontuario get(@PathVariable Long pacienteId) {
        return service.buscar(pacienteId);
    }

    @PutMapping
    public Prontuario salvar(@PathVariable Long pacienteId,
                             @RequestBody String texto) {
        return service.salvar(pacienteId, texto);
    }
}
