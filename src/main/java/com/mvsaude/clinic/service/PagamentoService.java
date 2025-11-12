package com.mvsaude.clinic.service;

import com.mvsaude.clinic.dto.PagamentoCreateDTO;
import com.mvsaude.clinic.dto.PagamentoDTO;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Pagamento;
import com.mvsaude.clinic.repository.ConsultaRepository;
import com.mvsaude.clinic.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repo;
    private final ConsultaRepository consultaRepo;

    @Transactional
    public PagamentoDTO registrar(PagamentoCreateDTO dto) {
        var consulta = consultaRepo.findById(dto.consultaId())
                .orElseThrow(() -> new BusinessException("Consulta inexistente"));

        var entidade = Pagamento.builder()
                .consulta(consulta)
                .valor(dto.valor())
                .forma(dto.forma())
                .observacao(dto.observacao())
                .dataPagamento(dto.dataPagamento() != null ? dto.dataPagamento() : LocalDateTime.now())
                .build();

        var salvo = repo.save(entidade);
        return toDTO(salvo);
    }

    public List<PagamentoDTO> listarPorConsulta(Long consultaId) {
        return repo.findByConsultaId(consultaId).stream().map(this::toDTO).toList();
    }

    /** 🔹 Gera um recibo texto (simples) para o pagamento. */
    public String gerarReciboTexto(Long id) {
        var p = repo.findById(id).orElseThrow(() -> new BusinessException("Pagamento não encontrado"));
        return """
            RECIBO DE PAGAMENTO
            -------------------
            ID Pagamento: %d
            Consulta ID: %d
            Valor: R$ %.2f
            Forma: %s
            Data: %s
            Observação: %s
            """.formatted(
                p.getId(),
                p.getConsulta().getId(),
                p.getValor(),
                p.getForma().name(),
                p.getDataPagamento(),
                p.getObservacao() == null ? "" : p.getObservacao()
        );
    }


    private PagamentoDTO toDTO(Pagamento p) {
        return new PagamentoDTO(
                p.getId(),
                p.getConsulta().getId(),
                p.getValor(),
                p.getForma().name(),
                p.getDataPagamento(),
                p.getObservacao()
        );
    }
}
