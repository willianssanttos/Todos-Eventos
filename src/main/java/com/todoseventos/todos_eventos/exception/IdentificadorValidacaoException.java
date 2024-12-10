package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class IdentificadorValidacaoException extends RuntimeException {

    public IdentificadorValidacaoException(){
        super(Constantes.IDENTIFICADOR_INVALIDO);
    }
}
