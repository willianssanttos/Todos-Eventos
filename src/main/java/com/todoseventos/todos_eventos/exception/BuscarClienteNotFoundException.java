package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class BuscarClienteNotFoundException extends RuntimeException{

    public BuscarClienteNotFoundException(){
        super(Constantes.ErrorRecuperarContas);
    }
}
