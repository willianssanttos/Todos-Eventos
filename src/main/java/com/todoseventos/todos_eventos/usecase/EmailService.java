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

    /**
     * Envia um e-mail de confirmação de inscrição.
     * @param destinatario O endereço de e-mail do destinatário.
     * @param assunto O assunto do e-mail.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     * @param dataEvento A data do evento.
     * @param localEvento O local do evento.
     */
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

    /**
     * Gera o corpo do e-mail de confirmação de inscrição.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     * @param dataEvento A data do evento.
     * @param localEvento O local do evento.
     * @return O corpo do e-mail em formato HTML.
     */
    private String gerarCorpoEmail(String nomePessoa, String nomeEvento, String dataEvento, String localEvento, String linkConfirmacao) {
        return "<html>" +
                "<body>" +
                "<h1>Inscrição Garantida!</h1>" +
                "<p>Olá, " + nomePessoa + ",</p>" +
                "<p>Sua inscrição no evento, " + nomeEvento + ", foi ganrantida! Não esqueca de confirma sua presença no botão abaixo.</p>" +
                "<h3>INFORMAÇÕES DO EVENTO</h3>" +
                "<p><strong>Data:</strong> " + dataEvento + "</p>" +
                "<p><strong>Local:</strong> " + localEvento + "</p>" +
                "</body>" +
                "</html>";
    }

    /**
     * Envia um e-mail de confirmação de participação.
     * @param destinatario O endereço de e-mail do destinatário.
     * @param assunto O assunto do e-mail.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     * @param dataEvento A data do evento.
     * @param endereco O endereço do evento.
     */
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

    /**
     * Gera o corpo do e-mail de confirmação de participação.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     * @param dataEvento A data do evento.
     * @param endereco O endereço do evento.
     * @return O corpo do e-mail em formato HTML.
     */
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

    /**
     * Envia um e-mail de cancelamento de evento.
     * @param destinatario O endereço de e-mail do destinatário.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     */
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

    /**
     * Gera o corpo do e-mail de cancelamento de evento.
     * @param nomePessoa O nome do destinatário.
     * @param nomeEvento O nome do evento.
     * @return O corpo do e-mail em formato HTML.
     */
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