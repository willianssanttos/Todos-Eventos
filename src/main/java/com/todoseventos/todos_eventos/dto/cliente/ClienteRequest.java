package com.todoseventos.todos_eventos.dto.cliente;

import com.todoseventos.todos_eventos.dto.Enuns.TipoClienteEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClienteRequest {

    private Long idPessoa;
    private String nome;
    private String cpf;
    private String cnpj;
    private String email;
    private String senha;
    private String telefone;
    private String dataNascimento;
    private TipoClienteEnum tipo_pessoa;
}
