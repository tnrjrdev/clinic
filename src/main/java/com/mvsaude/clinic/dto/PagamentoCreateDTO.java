package com.mvsaude.clinic.dto;

import com.mvsaude.clinic.model.Pagamento.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoCreateDTO(
        Long consultaId,
        BigDecimal valor,
        FormaPagamento forma,
        String observacao,
        LocalDateTime dataPagamento
) {}
