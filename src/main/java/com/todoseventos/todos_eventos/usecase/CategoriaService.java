package com.todoseventos.todos_eventos.usecase;

import com.todoseventos.todos_eventos.dao.CategoriaDao;
import com.todoseventos.todos_eventos.dto.CategoriaEnum;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaDao categoriaDao;


    public CategoriaModel procurarCategoriaPorNome(CategoriaEnum e){
        return categoriaDao.findNomeCategoria(e.name());
    }

    public CategoriaModel procurarCategoriaPorId(Integer id){
        return categoriaDao.findById(id);
    }
}
