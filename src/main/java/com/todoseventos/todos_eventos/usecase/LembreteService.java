package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.ParticipacaoDao;
import com.todoseventos.todos_eventos.dao.EventoDao;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LembreteService {

    @Autowired
    private ParticipacaoDao participacaoDao;

    @Autowired
    private EventoDao eventoDao;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 * * * *") // Executa a cada hora
    public void enviarLembretes() {
        List<ParticipacaoModel> participacoes = participacaoDao.findAll();

        for (ParticipacaoModel participacao : participacoes) {
            EventoModel evento = eventoDao.procurarPorId(participacao.getIdEvento());
            if (evento != null) {
                LocalDateTime dataHoraEvento = LocalDateTime.parse(evento.getDataHora_evento(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime agora = LocalDateTime.now();

                if (dataHoraEvento.minusDays(1).isBefore(agora) && dataHoraEvento.isAfter(agora)) {
                    // Enviar lembrete 1 dia antes
                 emailService.enviarEmail("email@exemplo.com", "Lembrete de Evento", "Lembre-se do evento " + evento.getNome_evento() + " que acontecerá em 1 dia.");
                } else if (dataHoraEvento.minusHours(4).isBefore(agora) && dataHoraEvento.isAfter(agora)) {
                    // Enviar lembrete 4 horas antes
                 emailService.enviarEmail("email@exemplo.com", "Lembrete de Evento", "Lembre-se do evento " + evento.getNome_evento() + " que acontecerá em 4 horas.");
                }
            }
        }
    }
}
