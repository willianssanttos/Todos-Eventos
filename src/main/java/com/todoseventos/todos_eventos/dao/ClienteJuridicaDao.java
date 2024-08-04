package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface ClienteJuridicaDao {
    ClienteJuridicaModel save(ClienteJuridicaModel pessoaJuridica);
    ClienteJuridicaModel update(ClienteJuridicaModel pessoaJuridica);
    ClienteJuridicaModel findByCnpj(String cnpj);
}
@Repository
class ClienteJuridicaDaoImpl implements ClienteJuridicaDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ClienteJuridicaModel save(ClienteJuridicaModel pessoaJuridica) {
        String sql = "SELECT inserir_cliente_juridico(?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaJuridica.getIdPessoa());
                ps.setString(2, pessoaJuridica.getCnpj());
                ps.execute();
                return null;
            });
            return pessoaJuridica;
        } catch (Exception e) {
            throw new CustomException("Erro ao salvar cliente jurídico: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteJuridicaModel update(ClienteJuridicaModel pessoaJuridica) {
        String sql = "SELECT atualizar_cliente_juridico(?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaJuridica.getIdPessoa());
                ps.setString(2, pessoaJuridica.getCnpj());
                ps.execute();
                return null;
            });
            return pessoaJuridica;
        } catch (Exception e) {
            throw new CustomException("Erro ao atualizar cliente jurídico: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ClienteJuridicaModel findByCnpj(String cnpj) {
        String sql = "SELECT * FROM procurar_cliente_juridico_por_cnpj(?)";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteJuridicaModel.class), cnpj);
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar cliente jurídico por CNPJ: " + e.getMessage());
        }
    }
}
