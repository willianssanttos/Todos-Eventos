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
public class TipoClienteModel implements Serializable {

    private Integer idTipoPessoa;
    private String nomeTipoPessoa;
}
