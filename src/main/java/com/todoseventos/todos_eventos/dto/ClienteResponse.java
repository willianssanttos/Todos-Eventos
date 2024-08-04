package com.todoseventos.todos_eventos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ClienteResponse {

    private Integer idPessoa;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private String dataNascimento;
    private String cnpj;
    private TipoClienteEnum tipo_pessoa;
}
