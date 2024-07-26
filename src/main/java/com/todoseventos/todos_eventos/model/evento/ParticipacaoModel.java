package com.todoseventos.todos_eventos.model.evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipacaoModel {
    private Long idParticipacao;
    private String cpf;
    private String cnpj;
    private Long idEvento;
    private String status;
}


