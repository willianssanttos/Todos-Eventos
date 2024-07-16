package com.todoseventos.todos_eventos.model.pessoa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PessoaModel  implements Serializable {

    private Long idPessoa;
    private String nome;
    private String cpf;
    private String cnpj;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate dataNascimento;
    private Integer tipo_pessoa;
}
