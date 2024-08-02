package com.todoseventos.todos_eventos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {

    private Long id;
    private String email;
    private String senha;
}
