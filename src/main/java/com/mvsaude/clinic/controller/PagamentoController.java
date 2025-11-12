package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.dto.PagamentoCreateDTO;
import com.mvsaude.clinic.dto.PagamentoDTO;
import com.mvsaude.clinic.model.Pagamento;
import com.mvsaude.clinic.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping
    public ResponseEntity<PagamentoDTO> registrar(@RequestBody PagamentoCreateDTO dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }

    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<List<PagamentoDTO>> porConsulta(@PathVariable Long consultaId) {
        return ResponseEntity.ok(service.listarPorConsulta(consultaId));
    }
}
