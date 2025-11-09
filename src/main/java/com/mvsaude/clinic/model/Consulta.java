package com.mvsaude.clinic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {

    public enum Status {
        AGENDADA,
        EM_ATENDIMENTO,
        CONCLUIDA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Future
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.AGENDADA;


}
