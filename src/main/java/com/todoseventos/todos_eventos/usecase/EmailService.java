package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(String destinatario, String assunto, String nomePessoa, String nomeEvento, String dataEvento, String localEvento, String linkConfirmacao) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(gerarCorpoEmail(nomePessoa, nomeEvento, dataEvento, localEvento, linkConfirmacao), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }

    private String gerarCorpoEmail(String nomePessoa, String nomeEvento, String dataEvento, String localEvento, String linkConfirmacao) {
        return "<html>" +
                "<body>" +
                "<h1>Inscrição Garantida!</h1>" +
                "<p>Olá, " + nomePessoa + ",</p>" +
                "<p>Sua inscrição no evento, " + nomeEvento + ", foi ganrantida! Não esqueca de confirma sua presença no botão abaixo.</p>" +
                "<h3>INFORMAÇÕES DO EVENTO</h3>" +
                "<p><strong>Data:</strong> " + dataEvento + "</p>" +
                "<p><strong>Local:</strong> " + localEvento + "</p>" +
                "<p><a href=\"" + linkConfirmacao + "\" style=\"display:inline-block;padding:10px 20px;margin:10px 0;border-radius:5px;background-color:#28a745;color:#ffffff;text-decoration:none;\">Confirmar participação</a></p>" +
                "</body>" +
                "</html>";
    }

    public void enviarEmailConfirmacao(String destinatario, String assunto, String nomePessoa, String nomeEvento, String dataEvento, EnderecoModel endereco) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(gerarCorpoEmailConfirmacao(nomePessoa, nomeEvento, dataEvento, endereco), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email de confirmação", e);
        }
    }

    private String gerarCorpoEmailConfirmacao(String nomePessoa, String nomeEvento, String dataEvento, EnderecoModel endereco) {
        return "<html>" +
                "<body>" +
                "<h1>Confirmação de Participação</h1>" +
                "<p>Olá, " + nomePessoa + ",</p>" +
                "<p>Agradecemos a confirmação no evento " + nomeEvento + ". Aguardamos a sua presença.</p>" +
                "<h3>INFORMAÇÕES DO EVENTO</h3>" +
                "<p><strong>Data:</strong> " + dataEvento + "</p>" +
                "<p><strong>Local:</strong> " + endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getBairro() + ", " + endereco.getCidade() + ", " + endereco.getUf() + "</p>" +
                "</body>" +
                "</html>";
    }

    public void enviarEmailCancelamento(String destinatario, String nomePessoa, String nomeEvento) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(destinatario);
            helper.setSubject("Evento Cancelado: " + nomeEvento);
            helper.setText(gerarCorpoEmailCancelamento(nomePessoa, nomeEvento), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email de cancelamento", e);
        }
    }

    private String gerarCorpoEmailCancelamento(String nomePessoa, String nomeEvento) {
        return "<html>" +
                "<body>" +
                "<h1>Evento Cancelado</h1>" +
                "<p>Olá, " + nomePessoa + ",</p>" +
                "<p>Informamos que o evento, " + nomeEvento + ", foi cancelado pelo produtor.</p>" +
                "<p>Pedimos desculpas pelo inconveniente.</p>" +
                "</body>" +
                "</html>";
    }
}