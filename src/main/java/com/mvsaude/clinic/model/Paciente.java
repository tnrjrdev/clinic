package com.mvsaude.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Size(min = 11, max = 14)
    @Column(unique = true, nullable = false)
    private String cpf;

    @Past
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Email
    @Column(length = 120)
    private String email;

    @Column(length = 20)
    private String telefone;

    //  endereço (para integração com ViaCEP)
    @Column(length = 9)
    private String cep;

    @Column(length = 120)
    private String logradouro;

    @Column(length = 80)
    private String complemento;

    @Column(length = 80)
    private String bairro;

    @Column(length = 80)
    private String localidade;

    @Column(length = 2)
    private String uf;
}
