package com.todoseventos.todos_eventos.repository.cliente;

import com.todoseventos.todos_eventos.model.cliente.ClienteFisicoModel;

public interface ClienteFisicaRepository {
    ClienteFisicoModel salvarCliFisico(ClienteFisicoModel pessoaFisica);
    ClienteFisicoModel atualizarCliFisico(ClienteFisicoModel pessoaFisica);
    ClienteFisicoModel procurarCpf(String cpf);
}
