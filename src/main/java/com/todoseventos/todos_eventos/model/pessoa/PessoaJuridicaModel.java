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
public class PessoaJuridicaModel implements Serializable {

    private Integer idPessoa;

    private String cnpj;
}
