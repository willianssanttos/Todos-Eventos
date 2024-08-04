package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface CategoriaDao {
    CategoriaModel procurarId(Integer idCategoria);
    CategoriaModel buscarNomeCategoria(String nomeCategoria);
}

@Repository
class CategoriaDaoImpl implements CategoriaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public CategoriaModel procurarId(Integer idCategoria) {
        String sql = "SELECT * FROM procurar_categoria_por_id(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), idCategoria);
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_POR_ID + e.getMessage());
        }
    }

    @Override
    @Transactional
    public CategoriaModel buscarNomeCategoria(String nomeCategoria) {
        String sql = "SELECT * FROM procurar_categoria_por_nome(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), nomeCategoria);
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_POR_NOME + e.getMessage());
        }
    }
}
