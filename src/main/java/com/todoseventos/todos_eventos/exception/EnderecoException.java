package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class EnderecoException extends RuntimeException{

    public EnderecoException(){
        super(Constantes.ERRO_SALVAR);
    }

}
