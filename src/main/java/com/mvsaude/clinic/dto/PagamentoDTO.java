package com.mvsaude.clinic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoDTO(
        Long id,
        Long consultaId,
        BigDecimal valor,
        String forma,
        LocalDateTime dataPagamento,
        String observacao
) {}
