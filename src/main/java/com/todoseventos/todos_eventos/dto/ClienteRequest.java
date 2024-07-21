package com.todoseventos.todos_eventos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ClienteRequest {

    private Long idPessoa;
    private String nome;
    private String cpf;
    private String cnpj;
    private String email;
    private String senha;
    private String telefone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String dataNascimento;
    private TipoClienteEnum tipo_pessoa;
}
