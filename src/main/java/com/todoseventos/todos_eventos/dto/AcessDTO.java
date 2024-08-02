package com.todoseventos.todos_eventos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcessDTO {

    //Retornar o usuario e liberações(authorities)

    private String token;

    public AcessDTO(String token){
        super();
        this.token = token;
    }
}
