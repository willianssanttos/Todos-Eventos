package com.todoseventos.todos_eventos.repository.evento;

import com.todoseventos.todos_eventos.model.evento.CategoriaModel;

public interface CategoriaRepository {
    CategoriaModel procurarId(Integer idCategoria);
    CategoriaModel buscarNomeCategoria(String nomeCategoria);
}

