package com.todoseventos.todos_eventos.model.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClienteFisicoModel extends ClienteModel{

    private String cpf;
    private String dataNascimento;
}
