package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class CpfNotFoundException extends RuntimeException{
    public CpfNotFoundException(){
        super(Constantes.CPF_JA_CADASTRADO);
    }

}
