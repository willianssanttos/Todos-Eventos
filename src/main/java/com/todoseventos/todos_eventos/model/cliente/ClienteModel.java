package com.todoseventos.todos_eventos.model.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClienteModel implements Serializable {

    private Integer idPessoa;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Integer tipo_pessoa;
    private ClienteFisicoModel clienteFisicaModel;
    private ClienteJuridicoModel clienteJuridicaModel;

}

