package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class ClienteException extends RuntimeException{

    public ClienteException(){
        super(Constantes.ERRO_SALVAR_CLIENTE);
    }
}
