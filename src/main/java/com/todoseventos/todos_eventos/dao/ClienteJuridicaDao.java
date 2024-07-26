package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.ClienteJuridicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteJuridicaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClienteJuridicaModel save(ClienteJuridicaModel pessoaJuridica){
        String sql = "INSERT INTO PESSOA_JURIDICA (id_pessoa, cnpj) VALUES (?,?)";
        jdbcTemplate.update(sql, pessoaJuridica.getIdPessoa(), pessoaJuridica.getCnpj());
        return pessoaJuridica;
    }

    public ClienteJuridicaModel update(ClienteJuridicaModel pessoaJuridica){
        String sql = "UPDATE PESSOA_JURIDICA SET cnpj = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoaJuridica.getCnpj(), pessoaJuridica.getIdPessoa());
        return pessoaJuridica;
    }

    public ClienteJuridicaModel findByCnpj(String cnpj){
        try {
            String sql = "SELECT pj.cnpj, p.nome, p.email FROM pessoa_juridica pj JOIN pessoa p ON pj.id_pessoa = p.id_pessoa WHERE pj.cnpj = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteJuridicaModel.class), cnpj);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

