package com.todoseventos.todos_eventos.repository.cliente;

import com.todoseventos.todos_eventos.model.cliente.TipoClienteModel;

public interface TipoClienteRepository {
    TipoClienteModel buscarPorNomeTipoPessoa(String nomeTipoPessoa);
}

