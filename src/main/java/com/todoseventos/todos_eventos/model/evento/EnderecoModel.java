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
public class EnderecoModel implements Serializable {

    private Long idEndereco;
    private Long idEvento;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
}