package com.todoseventos.todos_eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipacaoRequest {
    private String cpf;
    private String cnpj;
    private Long idEvento;
}
