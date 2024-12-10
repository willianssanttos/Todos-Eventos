package com.todoseventos.todos_eventos.repository.cliente.Impl;

import com.todoseventos.todos_eventos.repository.cliente.ClienteFisicaRepository;
import com.todoseventos.todos_eventos.exception.AtualizarContaException;
import com.todoseventos.todos_eventos.exception.BuscarClienteNotFoundException;
import com.todoseventos.todos_eventos.exception.ClienteException;
import com.todoseventos.todos_eventos.model.cliente.ClienteFisicoModel;
import com.todoseventos.todos_eventos.utils.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClienteFisicaRepositoryImpl implements ClienteFisicaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(ClienteFisicaRepositoryImpl.class);

    @Override
    @Transactional
    public ClienteFisicoModel salvarCliFisico(ClienteFisicoModel pessoaFisica) {
        logger.info(Constantes.DebugRegistroProcesso);

        try {
            String sql = "SELECT inserir_cliente_fisico(?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaFisica.getIdPessoa());
                ps.setString(2, pessoaFisica.getCpf());
                ps.setString(3, pessoaFisica.getDataNascimento());
                ps.execute();
                return null;
            });
            return pessoaFisica;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new ClienteException();
        }
    }

    @Override
    @Transactional
    public ClienteFisicoModel atualizarCliFisico(ClienteFisicoModel pessoaFisica) {
        logger.info(Constantes.DebugEditarProcesso);
        try {
            String sql = "SELECT atualizar_cliente_fisico(?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaFisica.getIdPessoa());
                ps.setString(2, pessoaFisica.getCpf());
                ps.setString(3,pessoaFisica.getDataNascimento());
                ps.execute();
                return null;
            });
            return pessoaFisica;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarContaException();
        }
    }

    @Override
    @Transactional
    public ClienteFisicoModel procurarCpf(String cpf) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_cliente_fisico_por_cpf(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteFisicoModel.class), cpf);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }
}