package com.todoseventos.todos_eventos.repository.cliente;

import com.todoseventos.todos_eventos.model.cliente.ClienteModel;
import java.util.List;

public interface ClienteRepository {
    ClienteModel procurarPorCpf(String cpf);
    ClienteModel procurarPorCnpj(String cnpj);
    ClienteModel procurarPorEmail(String email);
    ClienteModel salvarCliente(ClienteModel pessoa);
    ClienteModel atualizarCliente(ClienteModel pessoa);
    List<ClienteModel> listarTodasPessoas();
}
