package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.PessoaJuridicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaJuridicaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PessoaJuridicaModel save(PessoaJuridicaModel pessoaJuridica){
        String sql = "INSERT INTO PESSOA_JURIDICA (id_pessoa, cnpj) VALUES (?,?)";
        jdbcTemplate.update(sql, pessoaJuridica.getIdPessoa(), pessoaJuridica.getCnpj());
        return pessoaJuridica;
    }

    public PessoaJuridicaModel update(PessoaJuridicaModel pessoaJuridica){
        String sql = "UPDATE PESSOA_JURIDICA SET cnpj = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoaJuridica.getCnpj(), pessoaJuridica.getIdPessoa());
        return pessoaJuridica;
    }

    public PessoaJuridicaModel findByCnpj(String cnpj){
        String sql = "SELECT * FROM PESSOA_JURIDICA WHERE cnpj = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PessoaJuridicaModel.class), cnpj);
    }

    public boolean findById(Long id){
        String sql = "SELECT COUNT(*) FROM PESSOA_JURIDICA WHERE id_pessoa = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count !=null && count > 0;
    }
}
