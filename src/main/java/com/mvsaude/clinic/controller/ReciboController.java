package com.mvsaude.clinic.controller;

import com.mvsaude.clinic.service.ReciboService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recibos")
@RequiredArgsConstructor
public class ReciboController {

    private final ReciboService reciboService;

    /**
     * Gera o recibo em texto simples (para visualização rápida)
     */
    @GetMapping("/{pagamentoId}/texto")
    public ResponseEntity<String> gerarReciboTexto(@PathVariable Long pagamentoId) {
        String recibo = reciboService.gerarReciboTexto(pagamentoId);
        return ResponseEntity.ok(recibo);
    }

    /**
     * Gera o recibo em PDF para download
     */
    @GetMapping(value = "/{pagamentoId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarReciboPdf(@PathVariable Long pagamentoId) {
        byte[] pdf = reciboService.gerarReciboPdf(pagamentoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recibo-" + pagamentoId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    /**
     * Endpoint para testar ou listar todos os recibos (opcional)
     */
    @GetMapping
    public ResponseEntity<String> listarTodos() {
        return ResponseEntity.ok("Endpoint de listagem de recibos — em breve será implementado");
    }
}
