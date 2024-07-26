package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteFisicaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClienteFisicaModel save(ClienteFisicaModel pessoaFisica){
        String sql = "INSERT INTO PESSOA_FISICA (id_pessoa, cpf, dataNascimento) VALUES (?,?,?)";
        jdbcTemplate.update(sql, pessoaFisica.getIdPessoa(), pessoaFisica.getCpf(), pessoaFisica.getDataNascimento());
        return pessoaFisica;
    }

    public ClienteFisicaModel update(ClienteFisicaModel pessoaFisica){
        String sql = "UPDATE PESSOA_FISICA SET cpf = ?, dataNascimento = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoaFisica.getCpf(), pessoaFisica.getDataNascimento(), pessoaFisica.getIdPessoa());
        return pessoaFisica;
    }

    public ClienteFisicaModel findByCpf(String cpf) {
        try {
            String sql = "SELECT pf.cpf, pf.dataNascimento, p.nome, p.email FROM pessoa_fisica pf JOIN pessoa p ON pf.id_pessoa = p.id_pessoa WHERE pf.cpf = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteFisicaModel.class), cpf);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
