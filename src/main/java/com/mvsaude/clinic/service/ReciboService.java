package com.mvsaude.clinic.service;

import com.mvsaude.clinic.exception.BusinessException;
import com.mvsaude.clinic.model.Pagamento;
import com.mvsaude.clinic.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
public class ReciboService {

    private final PagamentoRepository pagamentoRepository;

    /**
     * Gera recibo simples em texto
     */
    public String gerarReciboTexto(Long pagamentoId) {
        Pagamento p = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));

        var consulta = p.getConsulta();
        var paciente = consulta.getPaciente();
        var medico = consulta.getMedico();
        var fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return """
                RECIBO DE PAGAMENTO - MV Clinic
                --------------------------------
                ID do pagamento: %d
                Data do pagamento: %s
                Valor: R$ %.2f
                Forma: %s
                Observação: %s

                Consulta: %d (%s)
                Médico: %s - %s
                Paciente: %s (CPF: %s)
                """.formatted(
                p.getId(),
                p.getDataPagamento().format(fmt),
                p.getValor(),
                p.getForma(),
                p.getObservacao() == null ? "-" : p.getObservacao(),
                consulta.getId(),
                consulta.getDataHora().format(fmt),
                medico.getNome(),
                medico.getEspecialidade(),
                paciente.getNome(),
                paciente.getCpf()
        );
    }

    /**
     * Gera recibo PDF real
     */
    public byte[] gerarReciboPdf(Long pagamentoId) {
        Pagamento p = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new BusinessException("Pagamento não encontrado"));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, baos);
            doc.open();

            doc.add(new Paragraph("RECIBO DE PAGAMENTO - MV Clinic"));
            doc.add(new Paragraph("--------------------------------"));
            doc.add(new Paragraph("ID do pagamento: " + p.getId()));
            doc.add(new Paragraph("Data do pagamento: " + p.getDataPagamento()));
            doc.add(new Paragraph("Valor: R$ " + p.getValor()));
            doc.add(new Paragraph("Forma: " + p.getForma()));
            if (p.getObservacao() != null)
                doc.add(new Paragraph("Observação: " + p.getObservacao()));

            doc.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        } catch (Exception e) {
            throw new BusinessException("Falha ao gerar recibo PDF: " + e.getMessage());
        }
    }
}
