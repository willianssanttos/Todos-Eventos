package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class NumeroCelularValidacaoException extends RuntimeException{
    public NumeroCelularValidacaoException(){
        super(Constantes.TELEFONE_INVALIDO);
    }
}