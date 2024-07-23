package com.todoseventos.todos_eventos.model.evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventoModel implements Serializable {

    private Long idEvento;
    private String nome_evento;
    private String dataHora_evento;
    private String descricao;
    private Integer id_categoria;
    private Long id_endereco;
}
