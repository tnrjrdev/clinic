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

    // (1) Criar pagamento
    @PostMapping
    public ResponseEntity<PagamentoDTO> registrar(@RequestBody PagamentoCreateDTO dto) {
        return ResponseEntity.ok(service.registrar(dto));
    }

    // (2) Listar pagamentos por consulta
    @GetMapping("/consulta/{consultaId}")
    public ResponseEntity<List<PagamentoDTO>> porConsulta(@PathVariable Long consultaId) {
        return ResponseEntity.ok(service.listarPorConsulta(consultaId));
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<String> recibo(@PathVariable Long id) {
        String pdfFake = service.gerarReciboTexto(id); // depois evoluímos para PDF real
        return ResponseEntity.ok(pdfFake);
    }

}

