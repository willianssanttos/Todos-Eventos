package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class EmailExistenteException extends RuntimeException{

    public EmailExistenteException(){
        super(Constantes.EmailJaCadastrado);
    }
}
