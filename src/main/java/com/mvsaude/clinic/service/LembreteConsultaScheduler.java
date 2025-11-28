package com.mvsaude.clinic.service;

import com.mvsaude.clinic.model.Consulta;
import com.mvsaude.clinic.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LembreteConsultaScheduler {

    private final ConsultaRepository consultaRepository;
    private final EmailService emailService;

    // roda todo dia às 08:00
    @Scheduled(cron = "0 0 8 * * *")
    public void enviarLembretesConsultasAmanha() {

        LocalDate amanha = LocalDate.now().plusDays(1);

        LocalDateTime inicio = amanha.atStartOfDay();
        LocalDateTime fim = amanha.atTime(LocalTime.MAX);

        List<Consulta> consultas = consultaRepository.consultasEntre(inicio, fim);

        for (Consulta consulta : consultas) {

            String email = consulta.getPaciente().getEmail();

            if (email == null || email.isBlank()) continue;

            String mensagem = """
                Olá, %s,

                Lembrete da sua consulta amanhã (%s) com o(a) médico(a) %s.

                Se precisar reagendar, entre em contato conosco.
            """.formatted(
                    consulta.getPaciente().getNome(),
                    consulta.getDataHora(),
                    consulta.getMedico().getNome()
            );

            emailService.enviarLembreteConsulta(email, mensagem);
        }
    }
}
