package com.todoseventos.todos_eventos.repository.cliente;

import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicoModel;

public interface ClienteJuridicaRepository {
    ClienteJuridicoModel salvarCliJuridico(ClienteJuridicoModel pessoaJuridica);
    ClienteJuridicoModel atualizarJuridico(ClienteJuridicoModel pessoaJuridica);
    ClienteJuridicoModel procurarCnpj(String cnpj);
}

