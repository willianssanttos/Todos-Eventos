package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.evento.CategoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public CategoriaModel findById(Integer idCategoria){
        String sql = "select * from categoria where id_categoria = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new CategoriaModel(rs.getInt("id_categoria"),
                rs.getString("nome_categoria")), idCategoria);
    }

    public CategoriaModel findNomeCategoria(String nomeCategoria){
        String sql = "select * from categoria where nome_categoria = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new CategoriaModel(rs.getInt("id_categoria"),
                rs.getString("nome_categoria")), nomeCategoria);
    }

    public void save(CategoriaModel e){
        String sql = "insert into categoria(nome_categoria) values(?)";
        jdbcTemplate.update(sql, e.getNomeCategoria());
    }
}
