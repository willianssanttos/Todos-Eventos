package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class AtualizarContaException extends RuntimeException{

    public AtualizarContaException(){
        super(Constantes.ERRO_ATUALIZAR_CLIENTE);
    }
}
