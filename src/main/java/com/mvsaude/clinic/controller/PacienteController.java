package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.PacienteDTO;
import com.mvsaude.clinic.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService service;

    @GetMapping
    public List<PacienteDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PacienteDTO obter(@PathVariable Long id) {
        return service.obter(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PacienteDTO criar(@RequestBody @Valid PacienteDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public PacienteDTO atualizar(@PathVariable Long id, @RequestBody @Valid PacienteDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
