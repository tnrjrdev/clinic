package com.mvsaude.clinic.service;

import com.mvsaude.clinic.dto.PacienteDTO;
import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.integracao.ViaCepClient;
import com.mvsaude.clinic.model.Paciente;
import com.mvsaude.clinic.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repo;
    private final ViaCepClient viaCepClient;
    private final AuditoriaService auditoria;

    public List<PacienteDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public PacienteDTO obter(Long id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new BusinessException("Paciente não encontrado"));
    }

    @Transactional
    public PacienteDTO criar(PacienteDTO dto) {
        repo.findByCpf(dto.cpf()).ifPresent(p -> {
            throw new BusinessException("CPF já cadastrado");
        });

        // Monta entidade a partir do DTO
        var p = Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(dto.dataNascimento())
                .email(dto.email())
                .telefone(dto.telefone())
                .cep(dto.cep())
                .logradouro(dto.logradouro())
                .complemento(dto.complemento())
                .bairro(dto.bairro())
                .localidade(dto.localidade())
                .uf(dto.uf())
                .build();

        // Se veio CEP mas não veio logradouro/bairro/localidade/uf, completa via ViaCEP
        if (isNotBlank(dto.cep()) && (isBlank(dto.logradouro()) || isBlank(dto.bairro()) || isBlank(dto.localidade()) || isBlank(dto.uf()))) {
            var resp = viaCepClient.buscar(dto.cep());
            if (resp == null || "true".equalsIgnoreCase(getField(resp, "erro"))) {
                throw new BusinessException("CEP inválido ou não encontrado");
            }
            p.setLogradouro(nvl(dto.logradouro(), resp.logradouro()));
            p.setBairro(nvl(dto.bairro(), resp.bairro()));
            p.setLocalidade(nvl(dto.localidade(), resp.localidade()));
            p.setUf(nvl(dto.uf(), resp.uf()));
            // complemento: mantém o do DTO se vier; se não vier, usa o do ViaCEP
            p.setComplemento(nvl(dto.complemento(), resp.complemento()));
        }

        var salvo = repo.save(p);

        auditoria.registrar("Paciente", "CRIAR", "PacienteId=" + salvo.getId() + ", cpf=" + salvo.getCpf());

        return toDTO(salvo);
    }

    @Transactional
    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        var p = repo.findById(id).orElseThrow(() -> new BusinessException("Paciente não encontrado"));

        p.setNome(dto.nome());
        p.setCpf(dto.cpf());
        p.setDataNascimento(dto.dataNascimento());
        p.setEmail(dto.email());
        p.setTelefone(dto.telefone());
        p.setCep(dto.cep());
        p.setLogradouro(dto.logradouro());
        p.setComplemento(dto.complemento());
        p.setBairro(dto.bairro());
        p.setLocalidade(dto.localidade());
        p.setUf(dto.uf());

        // Se atualizou CEP e faltou informação de endereço, tenta completar com ViaCEP
        if (isNotBlank(dto.cep()) && (isBlank(dto.logradouro()) || isBlank(dto.bairro()) || isBlank(dto.localidade()) || isBlank(dto.uf()))) {
            var resp = viaCepClient.buscar(dto.cep());
            if (resp == null || "true".equalsIgnoreCase(getField(resp, "erro"))) {
                throw new BusinessException("CEP inválido ou não encontrado");
            }
            p.setLogradouro(nvl(p.getLogradouro(), resp.logradouro()));
            p.setBairro(nvl(p.getBairro(), resp.bairro()));
            p.setLocalidade(nvl(p.getLocalidade(), resp.localidade()));
            p.setUf(nvl(p.getUf(), resp.uf()));
            p.setComplemento(nvl(p.getComplemento(), resp.complemento()));
        }

        var salvo = repo.save(p);

        auditoria.registrar("Paciente", "ATUALIZAR", "PacienteId=" + salvo.getId());

        return toDTO(salvo);
    }

    @Transactional
    public void remover(Long id) {
        if (!repo.existsById(id)) {
            throw new BusinessException("Paciente não encontrado");
        }
        repo.deleteById(id);

        auditoria.registrar("Paciente", "REMOVER", "PacienteId=" + id);
    }

    // --------- helpers ---------

    private PacienteDTO toDTO(Paciente p) {
        return new PacienteDTO(
                p.getId(),
                p.getNome(),
                p.getCpf(),
                p.getDataNascimento(),
                p.getEmail(),
                p.getTelefone(),
                p.getCep(),
                p.getLogradouro(),
                p.getComplemento(),
                p.getBairro(),
                p.getLocalidade(),
                p.getUf()
        );
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    private static String nvl(String prefer, String fallback) {
        return isNotBlank(prefer) ? prefer : fallback;
    }

    // Proteção para campos possivelmente nulos na resposta
    private static String getField(ViaCepClient.ViaCepResponse r, String field) {
        // Não usamos reflection para performance/simplicidade no runtime;
        // aqui só cobrimos "erro" se o client for adaptado para ler esse campo.
        return null;
    }
}
