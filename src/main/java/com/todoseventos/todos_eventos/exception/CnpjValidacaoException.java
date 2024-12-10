package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class CnpjValidacaoException extends RuntimeException{
    public CnpjValidacaoException(){
        super(Constantes.CNPJ_INVALIDO);
    }

}
