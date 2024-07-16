package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.PessoaFisicaModel;
import com.todoseventos.todos_eventos.model.pessoa.PessoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaFisicaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PessoaFisicaModel save(PessoaFisicaModel pessoaFisica){
        String sql = "INSERT INTO PESSOA_FISICA (id_pessoa, cpf, dataNascimento) VALUES (?,?,?)";
        jdbcTemplate.update(sql, pessoaFisica.getIdPessoa(), pessoaFisica.getCpf(), pessoaFisica.getDataNascimento());
        return pessoaFisica;
    }


    public PessoaFisicaModel update(PessoaFisicaModel pessoaFisica){
        String sql = "UPDATE PESSOA_FISICA SET cpf = ?, dataNascimento = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoaFisica.getCpf(), pessoaFisica.getDataNascimento(), pessoaFisica.getIdPessoa());
        return pessoaFisica;
    }

    public PessoaFisicaModel findByCpf(String cpf) {
        String sql = "SELECT * FROM PESSOA_FISICA WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PessoaFisicaModel.class), cpf);
    }

    public boolean findById(Long id){
        String sql = "SELECT COUNT(*) FROM PESSOA_FISICA WHERE id_pessoa = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count !=null && count > 0;
    }
}
