package com.mvsaude.clinic.repository;

import com.mvsaude.clinic.model.MedicoBloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MedicoBloqueioRepository extends JpaRepository<MedicoBloqueio, Long> {

    @Query("""
        select case when count(b) > 0 then true else false and
        from MedicoBloqueio b
        where b.medico.id = :medicoId
          and :dataHora between b.inicio and b.fim
    """)
    boolean existeBloqueio(@Param("medicoId") Long medicoId,
                           @Param("dataHora")LocalDateTime dataHora);
}
