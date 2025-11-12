package com.mvsaude.clinic.service;

import com.mvsaude.clinic.dto.PagamentoCreateDTO;
import com.mvsaude.clinic.dto.PagamentoDTO;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Consulta;
import com.mvsaude.clinic.model.Pagamento;
import com.mvsaude.clinic.repository.ConsultaRepository;
import com.mvsaude.clinic.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepo;
    private final ConsultaRepository consultaRepo;

    @Transactional
    public PagamentoDTO registrar(PagamentoCreateDTO dto) {
        var consulta = consultaRepo.findById(dto.consultaId())
                .orElseThrow(() -> new BusinessException("Consulta não encontrada"));

        if (consulta.getStatus() != Consulta.Status.CONCLUIDA) {
            throw new BusinessException("Pagamento só pode ser registrado para consultas CONCLUÍDAS");
        }

        var pagamento = Pagamento.builder()
                .consulta(consulta)
                .valor(dto.valor())
                .forma(dto.forma())
                .dataPagamento(LocalDateTime.now())
                .observacao(dto.observacao())
                .build();

        return toDTO(pagamentoRepo.save(pagamento));
    }

    public List<PagamentoDTO> listarPorConsulta(Long consultaId) {
        return pagamentoRepo.findByConsultaId(consultaId)
                .stream().map(this::toDTO).toList();
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
