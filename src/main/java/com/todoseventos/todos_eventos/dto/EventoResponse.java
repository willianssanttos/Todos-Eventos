package com.todoseventos.todos_eventos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventoResponse {

    private Integer idEvento;
    private String nome_evento;
    private String dataHora_evento;
    private String dataHora_eventofinal;
    private String descricao;
    private String status;
    private CategoriaEnum categoria;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
}
