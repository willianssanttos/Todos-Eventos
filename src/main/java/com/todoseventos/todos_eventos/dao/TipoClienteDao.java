package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.TipoClienteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TipoClienteDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TipoClienteModel findByNomeTipoPessoa(String nomeTipoPessoa) {
        String sql = "SELECT * FROM TIPO_PESSOA WHERE nome_tipo_pessoa = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TipoClienteModel.class), nomeTipoPessoa);
    }
}
