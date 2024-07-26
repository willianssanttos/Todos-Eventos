package com.todoseventos.todos_eventos.model.pessoa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ClienteFisicaModel implements Serializable {

    private Long idPessoa;
    private String cpf;
    private String dataNascimento;
    private String nome;
    private String email;

}
