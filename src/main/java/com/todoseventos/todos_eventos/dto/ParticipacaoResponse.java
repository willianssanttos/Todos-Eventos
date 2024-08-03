package com.todoseventos.todos_eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipacaoResponse {

    private Integer idParticipacao;
    private String nomePessoa;
    private String emailPessoa;
    private String cpfPessoa;
    private String cnpjPessoa;
    private String nomeEvento;
    private String dataHoraEvento;
    private String dataHoraEventoFinal;
    private String status;
    private String localEvento;

}