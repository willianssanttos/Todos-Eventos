package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class CpfValidacaoException extends RuntimeException{

    public CpfValidacaoException(){
        super(Constantes.CPF_INVALIDO);
    }

}
