package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class CnpjNotFoundException extends RuntimeException{

    public CnpjNotFoundException(){
        super(Constantes.CNPJ_JA_CADASTRADO);
    }

}
