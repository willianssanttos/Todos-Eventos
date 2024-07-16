package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.TipoPessoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TipoPessoaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TipoPessoaModel findByNomeTipoPessoa(String nomeTipoPessoa) {
        String sql = "SELECT * FROM TIPO_PESSOA WHERE nome_tipo_pessoa = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(TipoPessoaModel.class), nomeTipoPessoa);
    }
}
