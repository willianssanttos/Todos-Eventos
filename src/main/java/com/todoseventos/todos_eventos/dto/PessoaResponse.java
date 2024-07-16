package com.todoseventos.todos_eventos.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PessoaResponse {

    private Long idPessoa;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String cnpj;
    private TipoPessoaEnum tipo_pessoa;
}
