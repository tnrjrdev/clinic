package com.mvsaude.clinic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record PacienteDTO(
        Long id,

        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
        String cpf,

        @Past
        LocalDate dataNascimento,

        // contato
        @Email
        String email,
        String telefone,

        //  endereço (para integração com ViaCEP)
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf
) {}
