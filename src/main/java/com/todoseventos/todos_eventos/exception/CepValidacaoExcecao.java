package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class CepValidacaoExcecao extends RuntimeException{

    public CepValidacaoExcecao(){
        super(Constantes.CEP_INVALIDO);
    }
}
