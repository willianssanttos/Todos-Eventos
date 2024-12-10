package com.todoseventos.todos_eventos.repository.evento.Impl;

import com.todoseventos.todos_eventos.repository.evento.EventoRepository;
import com.todoseventos.todos_eventos.exception.*;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
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
import java.util.Optional;

@Repository
public class EventoRepositoryImpl implements EventoRepository {

    private static final Logger logger = LoggerFactory.getLogger(EventoRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public EventoModel salvarEvento(EventoModel evento) {
        logger.info(Constantes.DebugRegistroProcesso);
        try {
            String sql = "SELECT inserir_evento(?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, evento);
                ps.execute();
                return null;
            });
            Integer idEvento = jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('evento','id_evento'))", Integer.class);
            evento.setIdEvento(idEvento);
            return evento;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new EventoException();
        }
    }

    @Override
    @Transactional
    public EventoModel atualizarEvento(EventoModel evento) {
        logger.info(Constantes.DebugEditarProcesso);
        try {
            String sql = "SELECT atualizar_evento(?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, evento.getIdEvento());
                ps.setString(2, evento.getNome_evento().trim());
                ps.setString(3, evento.getDataHora_evento());
                ps.setString(4, evento.getDataHora_eventofinal());
                ps.setString(5, evento.getDescricao().trim());
                ps.setString(6, evento.getStatus().trim());
                ps.setInt(7, evento.getId_categoria());
                ps.execute();
                return null;
            });
            return evento;
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroRegistrarNoServidor, e.getMessage());
            throw new AtualizarEventoException();
        }
    }


    @Override
    @Transactional
    public Optional<EventoModel> procurarPorNome(String nomeEvento) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_evento_por_nome(?)";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), nomeEvento.trim()));
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarEventoNotFoundException();
        }
    }

    @Override
    @Transactional
    public Optional<EventoModel> procurarPorId(Integer idEvento) {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM procurar_evento_por_id(?)";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), idEvento));
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarEventoNotFoundException();
        }
    }

    @Override
    @Transactional
    public List<EventoModel> localizarEvento() {
        logger.info(Constantes.DebugBuscarProcesso);
        try {
            String sql = "SELECT * FROM localizar_evento()";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventoModel.class));
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroBuscarRegistroNoServidor, e.getMessage());
            throw new BuscarEventoNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deletarPorId(Integer idEvento) {
        logger.info(Constantes.DebugDeletarProcesso);
        try {
            String sql = "SELECT deletar_evento(?)";
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, idEvento);
                ps.execute();
                return null;
            });
        } catch (DataAccessException e) {
            logger.error(Constantes.ErroDeletarRegistroNoServidor, e.getMessage());
            throw new DeletarEventoException();
        }
    }

    private void setPreparedStatementParameters(PreparedStatement ps, EventoModel evento) throws SQLException {
        ps.setString(1, evento.getNome_evento().trim());
        ps.setString(2, evento.getDataHora_evento());
        ps.setString(3, evento.getDataHora_eventofinal());
        ps.setString(4, evento.getDescricao().trim());
        ps.setString(5, evento.getStatus().trim());
        ps.setInt(6, evento.getId_categoria());
    }
}