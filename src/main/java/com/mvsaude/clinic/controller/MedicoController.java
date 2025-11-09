package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.MedicoDTO;
import com.mvsaude.clinic.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {
    private final MedicoService service;

    @GetMapping
    public List<MedicoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MedicoDTO obter(@PathVariable Long id) {
        return service.obter(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoDTO criar(@RequestBody @Valid MedicoDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public MedicoDTO atualizar(@PathVariable Long id, @RequestBody @Valid MedicoDTO dto) {
        return service.atualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
