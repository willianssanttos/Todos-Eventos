package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class EmailValidacaoException extends RuntimeException{
    public EmailValidacaoException(){
        super(Constantes.EMAIL_INVALIDO);
    }
}
