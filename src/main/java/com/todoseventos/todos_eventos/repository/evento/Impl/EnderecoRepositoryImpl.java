package com.todoseventos.todos_eventos.repository.evento.Impl;

import com.todoseventos.todos_eventos.repository.evento.EnderecoRepository;
import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.evento.EnderecoModel;
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class EnderecoRepositoryImpl implements EnderecoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final Logger logger = LoggerFactory.getLogger(EnderecoRepositoryImpl.class);

    @Override
    @Transactional
    public EnderecoModel salverEndereco(EnderecoModel endereco) {
        logger.info(Constantes.DebugRegistroProcesso);
        try {
            String sql = "SELECT inserir_endereco(?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new EnderecoException();
        }
    }


    @Override
    @Transactional
    public EnderecoModel atualizarEndereco(EnderecoModel endereco) {
        logger.info(Constantes.DebugEditarProcesso);
        try {
            String sql = "SELECT atualizar_endereco(?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, endereco);
                ps.execute();
                return null;
            });
            return endereco;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarEnderecoException();
        }
    }

    @Override
    @Transactional
    public Optional<EnderecoModel> procurarPorIdEvento(Integer id) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_endereco_por_id_evento(?)";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EnderecoModel.class), id));
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarEnderecoNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deletarPorIdEvento(Integer idEvento) {
        logger.info(Constantes.DebugDeletarProcesso);
        try {
            String sql = "SELECT deletar_endereco(?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, idEvento);
                ps.execute();
                return null;
            });
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroDeletarRegistroNoServidor, e.getMessage());
            throw new DeletarEnderecoException();
        }
    }

    private void setPreparedStatementParameters(PreparedStatement ps, EnderecoModel endereco) throws SQLException {
        ps.setString(1, endereco.getRua());
        ps.setString(2, endereco.getNumero());
        ps.setString(3, endereco.getBairro());
        ps.setString(4, endereco.getCidade());
        ps.setString(5, endereco.getUf());
        ps.setString(6, endereco.getCep());
        ps.setInt(7, endereco.getIdEvento());
    }
}