package com.mvsaude.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PacienteDTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @Past
        LocalDate dataNascimento
) {}
