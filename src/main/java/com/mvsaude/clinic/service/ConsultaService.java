package com.mvsaude.clinic.service;

import com.mvsaude.clinic.dto.ConsultaDTO;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Consulta;
import com.mvsaude.clinic.repository.ConsultaRepository;
import com.mvsaude.clinic.repository.MedicoRepository;
import com.mvsaude.clinic.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {
    private final ConsultaRepository repo;
    private final PacienteRepository pacienteRepo;
    private  final MedicoRepository medicoRepo;

    public List<ConsultaDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public List<ConsultaDTO> futurasPorMedico(Long medicoId) {
        return repo.futurasPorMedico(medicoId, LocalDateTime.now()).stream().map(this::toDTO).toList();
    }

    public List<ConsultaDTO> porPaciente(Long pacienteId) {
        return repo.porPaciente(pacienteId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public ConsultaDTO agendar(ConsultaDTO dto) {
        var paciente = pacienteRepo.findById(dto.pacienteId()).orElseThrow(() -> new BusinessException("Paciente inexistente"));
        var medico = medicoRepo.findById(dto.medicoId()).orElseThrow(() -> new BusinessException("Médico inexistente"));
        if (dto.dataHora().isBefore(LocalDateTime.now())) throw  new BusinessException("Data/hora deve ser futura");
        if (repo.existeConflito(medico.getId(), dto.dataHora())) throw new BusinessException("Médico já possui consulta nesse horário");
        var c = Consulta.builder().dataHora(dto.dataHora()).paciente(paciente).medico(medico).status(Consulta.Status.AGENDADA).build();
        return toDTO(repo.save(c));
    }

    @Transactional
    public ConsultaDTO atualizar(Long id, ConsultaDTO dto) {
        var c = repo.findById(id).orElseThrow(() -> new BusinessException("Consulta não encontrada"));
        if (dto.dataHora() !=null) {
            if (dto.dataHora().isBefore(LocalDateTime.now())) throw new BusinessException("Data/hora deve ser futura");
            if (repo.existeConflito(c.getMedico().getId(), dto.dataHora())) throw new BusinessException("Médico já possui consulta nesse horário");
            c.setDataHora(dto.dataHora());
        }
        return toDTO(repo.save(c));
    }

    @Transactional
    public void cancelar(Long id) {
        var c = repo.findById(id).orElseThrow(() -> new BusinessException("Consulta não encontrada"));
        c.setStatus(Consulta.Status.CANCELADA);
        repo.save(c);
    }

    @Transactional
    public ConsultaDTO iniciarAtendimento(Long id) {
        var c = repo.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta não encontrada"));
        if (c.getStatus() != Consulta.Status.AGENDADA) {
            throw new BusinessException("Só é possível iniciar consulta AGENDADAS");
        }
        c.setStatus(Consulta.Status.EM_ATENDIMENTO);
        return toDTO(repo.save(c));
    }

    @Transactional
    public ConsultaDTO concluir(Long id) {
        var c = repo.findById(id)
                .orElseThrow(() -> new BusinessException("Consulta não encontrada"));
        if (c.getStatus() != Consulta.Status.EM_ATENDIMENTO) {
            throw new BusinessException("Só é possível concluir consultas EM_ATENDIMENTO");
        }
        c.setStatus(Consulta.Status.CONCLUIDA);
        return toDTO(repo.save(c));
    }

    public List<ConsultaDTO> historicoPaciente(Long pacienteId) {
        return repo.historicoPorPaciente(pacienteId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ConsultaDTO toDTO(Consulta c) {
        return new ConsultaDTO(c.getId(), c.getDataHora(), c.getPaciente().getId(), c.getMedico().getId(), c.getStatus().name());
    }
}
