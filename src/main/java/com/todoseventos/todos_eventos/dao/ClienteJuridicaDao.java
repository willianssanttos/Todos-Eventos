package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
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
        try {
            jdbcTemplate.update(sql, pessoaJuridica.getIdPessoa(), pessoaJuridica.getCnpj());
            return pessoaJuridica;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar cliente jurídico: " + e.getMessage());
        }
    }

    public ClienteJuridicaModel update(ClienteJuridicaModel pessoaJuridica){
        String sql = "UPDATE PESSOA_JURIDICA SET cnpj = ? WHERE id_pessoa = ?";
        try {
            jdbcTemplate.update(sql, pessoaJuridica.getCnpj(), pessoaJuridica.getIdPessoa());
            return pessoaJuridica;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar cliente jurídico: " + e.getMessage());
        }
    }

    public ClienteJuridicaModel findByCnpj(String cnpj){
        String sql = "SELECT pj.cnpj, p.nome, p.email FROM pessoa_juridica pj JOIN pessoa p ON pj.id_pessoa = p.id_pessoa WHERE pj.cnpj = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteJuridicaModel.class), cnpj);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Cliente jurídico não encontrado pelo CNPJ: " + cnpj);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar cliente jurídico por CNPJ: " + e.getMessage());
        }
    }
}