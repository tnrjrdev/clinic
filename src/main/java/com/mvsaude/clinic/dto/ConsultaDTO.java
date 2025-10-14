package com.mvsaude.clinic.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultaDTO(
        Long id,
        @NotNull
        LocalDateTime dataHora,
        @NotNull
        Long pacienteId,
        @NotNull
        Long medicoId,
        String status
) {}
