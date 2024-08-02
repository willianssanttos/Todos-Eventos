package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.cliente.ClienteFisicaModel;
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
        try {
            jdbcTemplate.update(sql, pessoaFisica.getIdPessoa(), pessoaFisica.getCpf(), pessoaFisica.getDataNascimento());
            return pessoaFisica;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar cliente físico: " + e.getMessage());
        }
    }

    public ClienteFisicaModel update(ClienteFisicaModel pessoaFisica){
        String sql = "UPDATE PESSOA_FISICA SET cpf = ?, dataNascimento = ? WHERE id_pessoa = ?";
        try {
            jdbcTemplate.update(sql, pessoaFisica.getCpf(), pessoaFisica.getDataNascimento(), pessoaFisica.getIdPessoa());
            return pessoaFisica;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar cliente físico: " + e.getMessage());
        }
    }

    public ClienteFisicaModel findByCpf(String cpf) {
        String sql = "SELECT pf.cpf, pf.dataNascimento, p.nome, p.email FROM pessoa_fisica pf JOIN pessoa p ON pf.id_pessoa = p.id_pessoa WHERE pf.cpf = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteFisicaModel.class), cpf);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Cliente físico não encontrado pelo CPF: " + cpf);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar cliente físico por CPF: " + e.getMessage());
        }
    }
}