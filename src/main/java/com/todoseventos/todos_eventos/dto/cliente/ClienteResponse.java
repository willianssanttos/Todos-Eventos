package com.todoseventos.todos_eventos.dto.cliente;

import com.todoseventos.todos_eventos.dto.Enuns.TipoClienteEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClienteResponse {

    private Integer idPessoa;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private TipoClienteEnum tipo_pessoa;
    private String cpf;
    private String dataNascimento;
    private String cnpj;
}
