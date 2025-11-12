package com.mvsaude.clinic.repository;

import com.mvsaude.clinic.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByConsultaId(Long id);
}
