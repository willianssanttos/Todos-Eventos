package com.todoseventos.todos_eventos.dto;

import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipacaoResponse {

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