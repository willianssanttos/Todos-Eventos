package com.todoseventos.todos_eventos.repository.evento;

import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import java.util.List;

public interface ParticipacaoRepository {
    ParticipacaoModel salvarParticipacao(ParticipacaoModel participacao);
    ParticipacaoModel atualizarParticipacao(ParticipacaoModel participacao);
    ParticipacaoModel localizarPorId(Integer idParticipacao);
    List<ParticipacaoModel> localizarPorIdEvento(Integer idEvento);
}

