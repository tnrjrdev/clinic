package com.mvsaude.clinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarLembreteConsulta(String para, String mensagem) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(para);
        mail.setSubject("Lembrete de Consulta");
        mail.setText(mensagem);
        mailSender.send(mail);
    }
}