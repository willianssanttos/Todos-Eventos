package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class ClienteNotFoundException extends RuntimeException{

    public ClienteNotFoundException(){
        super(Constantes.CLIENTE_NAO_ENCONTRADO);
    }

}
