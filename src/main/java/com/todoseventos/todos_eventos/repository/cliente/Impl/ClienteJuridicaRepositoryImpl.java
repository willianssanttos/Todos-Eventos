package com.todoseventos.todos_eventos.repository.cliente.Impl;

import com.todoseventos.todos_eventos.repository.cliente.ClienteJuridicaRepository;
import com.todoseventos.todos_eventos.exception.AtualizarContaException;
import com.todoseventos.todos_eventos.exception.BuscarClienteNotFoundException;
import com.todoseventos.todos_eventos.exception.ClienteException;
import com.todoseventos.todos_eventos.model.cliente.ClienteJuridicoModel;
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
public class ClienteJuridicaRepositoryImpl implements ClienteJuridicaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(ClienteJuridicaRepositoryImpl.class);

    @Override
    @Transactional
    public ClienteJuridicoModel salvarCliJuridico(ClienteJuridicoModel pessoaJuridica) {
        logger.info(Constantes.DebugRegistroProcesso);
        try {
            String sql = "SELECT inserir_cliente_juridico(?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaJuridica.getIdPessoa());
                ps.setString(2, pessoaJuridica.getCnpj());
                ps.execute();
                return null;
            });
            return pessoaJuridica;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new ClienteException();
        }
    }

    @Override
    @Transactional
    public ClienteJuridicoModel atualizarJuridico(ClienteJuridicoModel pessoaJuridica) {
        logger.info(Constantes.DebugEditarProcesso);

        try {
            String sql = "SELECT atualizar_cliente_juridico(?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, pessoaJuridica.getIdPessoa());
                ps.setString(2, pessoaJuridica.getCnpj());
                ps.execute();
                return null;
            });
            return pessoaJuridica;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarContaException();
        }
    }

    @Override
    @Transactional
    public ClienteJuridicoModel procurarCnpj(String cnpj) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_cliente_juridico_por_cnpj(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ClienteJuridicoModel.class), cnpj);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarClienteNotFoundException();
        }
    }
}