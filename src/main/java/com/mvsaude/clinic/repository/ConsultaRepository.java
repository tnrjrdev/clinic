package com.mvsaude.clinic.repository;

import com.mvsaude.clinic.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    @Query("select c from Consulta c where c.medico.id = :medicoId and c.dataHora >= :agora order by c.dataHora asc")
    List<Consulta> futurasPorMedico(@Param("medicoId") Long medicoId, @Param("agora")LocalDateTime agora);

    @Query("select c from Consulta c where c.paciente.id = :pacienteId order by c.dataHora desc")
    List<Consulta> porPaciente(@Param("pacienteId") Long pacienteId);

    @Query("select count(c) > 0 from Consulta c where c.medico.id = :medicoId and c.dataHora = :dataHora and c.status <> 'CANCELADA'")
    boolean existeConflito(@Param("medicoId") Long medicoId, @Param("dataHora") LocalDateTime dataHora);

    @Query("select c from Consulta c where c.paciente.id = :pacienteId order by c.dataHora desc")
    List<Consulta> historicoPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("""
            select c 
            from Consulta c
            where c.dataHora between :inicio and :fim
            order by c.dataHora
            """)
    List<Consulta> consultasDoDia(@Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim);

    @Query("""
            select c
            from Consulta c
            where c.medico.id = :medicoId
             and c.dataHora between :inicio and :fim
            order by c.dataHora
            """)
    List<Consulta> consultasdoDiaPorMedico(@Param("medicoId") Long medicoId,
                                          @Param("inicio") LocalDateTime inicio,
                                          @Param("fim") LocalDateTime fim);
}

