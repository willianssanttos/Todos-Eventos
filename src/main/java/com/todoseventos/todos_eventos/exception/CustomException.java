package com.todoseventos.todos_eventos.exception;

import org.springframework.stereotype.Component;

@Component
public class Excecoes  extends  RuntimeException {

    public Excecoes(String message){
        super(message);
    }
}
