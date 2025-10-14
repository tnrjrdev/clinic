package com.mvsaude.clinic.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicoDTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String especialidade,
        @NotBlank
        String crm
) {}
