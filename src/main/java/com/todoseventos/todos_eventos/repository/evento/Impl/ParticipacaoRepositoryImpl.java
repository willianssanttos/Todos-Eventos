package com.todoseventos.todos_eventos.repository.evento.Impl;

import com.todoseventos.todos_eventos.repository.evento.ParticipacaoRepository;
import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.evento.ParticipacaoModel;
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
import java.util.List;

@Repository
public class ParticipacaoRepositoryImpl implements ParticipacaoRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ParticipacaoRepositoryImpl.class);

    @Override
    @Transactional
    public ParticipacaoModel salvarParticipacao(ParticipacaoModel participacao) {
        logger.info(Constantes.DebugRegistroProcesso);
        try {
            String sql = "SELECT inserir_participacao(?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, participacao);
                ps.execute();
                return null;
            });
            Integer idParticipacao = jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('participacao','id_participacao'))", Integer.class);
            participacao.setIdParticipacao(idParticipacao);
            return participacao;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new ParticipacaoException();
        }
    }

    @Override
    @Transactional
    public ParticipacaoModel atualizarParticipacao(ParticipacaoModel participacao) {
        logger.info(Constantes.DebugEditarProcesso);
        try {
            String sql = "SELECT atualizar_participacao(?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, participacao.getIdParticipacao());
                ps.setString(2, participacao.getStatus());
                ps.execute();
                return null;
            });
            return participacao;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarParticipacaoException();
        }
    }

    @Override
    @Transactional
    public ParticipacaoModel localizarPorId(Integer idParticipacao) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_participacao_por_id(?)";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idParticipacao);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarParticipacaoNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<ParticipacaoModel> localizarPorIdEvento(Integer idEvento) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_participacoes_por_id_evento(?)";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ParticipacaoModel.class), idEvento);
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarParticipacaoNotFoundException();
        }
    }

    private void setPreparedStatementParameters(PreparedStatement ps, ParticipacaoModel participacao) throws SQLException {
        ps.setString(1, participacao.getCpf());
        ps.setString(2, participacao.getCnpj());
        ps.setInt(3, participacao.getIdEvento());
        ps.setString(4, participacao.getStatus());
    }
}

