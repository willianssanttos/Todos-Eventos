package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.model.pessoa.PessoaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PessoaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PessoaModel procurarPorCpf(String cpf) {
        String sql = "SELECT p.* FROM PESSOA p" +
                "INNER JOIN PESSOA_FISICA pf ON p.id_pessoa = pf.id_pessoa " +
                "WHERE pf.cpf = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<PessoaModel>(PessoaModel.class), cpf);
    }

    public PessoaModel procurarPorCnpj(String cnpj) {
        String sql = "SELECT p.* FROM PESSOA p INNER JOIN PESSOA_JURIDIA pj ON p.id_pessoa WHERE pj.cnpj = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<PessoaModel>(PessoaModel.class), cnpj);
    }

    public PessoaModel procurarPorId(Long id){
        String sql = "SELECT * FROM PESSOA WHERE id_pessoa = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PessoaModel.class), id);
    }

    public PessoaModel save(PessoaModel pessoa){
        String sql = "INSERT INTO PESSOA (nome, email, senha, telefone, id_tipo_pessoa) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getTipo_pessoa());
        return pessoa;
    }

    public PessoaModel update(PessoaModel pessoa){
        String sql = "UPDATE PESSOA SET nome = ?, email = ?, senha = ?, telefone = ?, id_tipo_pessoa = ? WHERE id_pessoa = ?";
        jdbcTemplate.update(sql, pessoa.getNome(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getTelefone(), pessoa.getIdPessoa());
        return pessoa;
    }

    public List<PessoaModel> findAll(){
        String sql = "SELECT * FROM PESSOA";
        List<PessoaModel> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PessoaModel>(PessoaModel.class));
        return query;
    }
}
