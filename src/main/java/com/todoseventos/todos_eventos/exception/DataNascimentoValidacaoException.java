package com.todoseventos.todos_eventos.exception;

import com.todoseventos.todos_eventos.utils.Constantes;

public class DataNascimentoValidacaoException extends RuntimeException{

    public DataNascimentoValidacaoException(){
        super(Constantes.DATA_NASCIMENTO_INVALIDA);
    }
}

