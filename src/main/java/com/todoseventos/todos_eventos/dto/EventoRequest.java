package com.todoseventos.todos_eventos.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventoRequest {

    private Long idEvento;
    private String nome_evento;
    private String dataHora_evento;
    private String descricao;
    private CategoriaEnum categoria;
    private Integer idEndereco;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
}
