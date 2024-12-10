package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class TipoCategoriaValidacaoException extends RuntimeException{
    public TipoCategoriaValidacaoException(){
        super(Constantes.TIPO_CATEGORIA_INVALIDO);
    }

}
