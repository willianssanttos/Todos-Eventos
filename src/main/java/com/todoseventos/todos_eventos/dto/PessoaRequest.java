package com.todoseventos.todos_eventos.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PessoaRequest {

    private Long idPessoa;
    private String nome;
    private String cpf; // campo para pessoa física
    private String cnpj; // campo para pessoa jurídica
    private String email;
    private String senha;
    private String telefone;
    private LocalDate dataNascimento;
    private TipoPessoaEnum tipo_pessoa;



}
