package com.mvsaude.clinic.dto;

import com.mvsaude.clinic.model.Pagamento.FormaPagamento;

import java.math.BigDecimal;

public record PagamentoCreateDTO(
        Long consultaId,
        BigDecimal valor,
        FormaPagamento forma,
        String observacao
) {}
