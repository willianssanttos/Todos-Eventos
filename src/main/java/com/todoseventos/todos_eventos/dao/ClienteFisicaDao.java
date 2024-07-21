package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.ClienteFisicaModel;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql = "UPDATE PESSOA_FISICA SET cpf = ?, dataNascimento = ? WHERE cpf = ?";
        jdbcTemplate.update(sql, pessoaFisica.getDataNascimento(), pessoaFisica.getCpf());
        return pessoaFisica;
    }


    public ClienteFisicaModel findByCpf(String cpf) {
        try {
            String sql = "SELECT * FROM PESSOA_FISICA WHERE cpf = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteFisicaModel.class), cpf);
        } catch (Exception e) {
            return null;
        }
    }
}
