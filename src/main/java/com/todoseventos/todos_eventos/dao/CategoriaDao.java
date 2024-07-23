package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CategoriaModel findById(Integer idCategoria) {
        String sql = "SELECT * FROM CATEGORIA WHERE id_categoria = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), idCategoria);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar categoria por ID: " + e.getMessage());
        }
    }

    public CategoriaModel findNomeCategoria(String nomeCategoria) {
        String sql = "SELECT * FROM CATEGORIA WHERE nome_categoria = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CategoriaModel.class), nomeCategoria);
    }

    public void save(CategoriaModel e) {
        String sql = "INSERT INTO CATEGORIA (nome_categoria) VALUES (?)";
        jdbcTemplate.update(sql, e.getNomeCategoria());
    }
}
