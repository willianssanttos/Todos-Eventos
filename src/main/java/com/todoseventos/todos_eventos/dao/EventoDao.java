package com.todoseventos.todos_eventos.dao;

import com.todoseventos.todos_eventos.exception.CustomException;
import com.todoseventos.todos_eventos.model.evento.EventoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EventoDao {
    EventoModel salvarEvento(EventoModel evento);
    EventoModel atualizarEvento(EventoModel evento);
    Optional<EventoModel> procurarPorNome(String nomeEvento);
    Optional<EventoModel> procurarPorId(Integer idEvento);
    List<EventoModel> localizarEvento();
    void deletarPorId(Integer idEvento);
}

@Repository
class EventoDaoImpl implements EventoDao {

    private static final Logger logger = LoggerFactory.getLogger(EventoDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public EventoModel salvarEvento(EventoModel evento) {
        String sql = "SELECT inserir_evento(?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                setPreparedStatementParameters(ps, evento);
                ps.execute();
                return null;
            });
            Integer idEvento = jdbcTemplate.queryForObject("SELECT currval(pg_get_serial_sequence('evento','id_evento'))", Integer.class);
            evento.setIdEvento(idEvento);
            return evento;
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_SALVAR + e.getMessage());
        }
    }

    @Override
    @Transactional
    public EventoModel atualizarEvento(EventoModel evento) {
        String sql = "SELECT atualizar_evento(?, ?, ?, ?, ?, ?, ?)";
        try {
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
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_ATUALIZAR + e.getMessage());
        }
    }


    @Override
    @Transactional
    public Optional<EventoModel> procurarPorNome(String nomeEvento) {
        String sql = "SELECT * FROM procurar_evento_por_nome(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), nomeEvento.trim()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_POR_NOME + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<EventoModel> procurarPorId(Integer idEvento) {
        String sql = "SELECT * FROM procurar_evento_por_id(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(EventoModel.class), idEvento));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_BUSCAR_POR_ID+ e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<EventoModel> localizarEvento() {
        String sql = "SELECT * FROM localizar_evento()";
        logger.info("Executando SQL para buscar evento: {}", sql);
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventoModel.class));
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_LISTAR_TODOS + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deletarPorId(Integer idEvento) {
        String sql = "SELECT deletar_evento(?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setInt(1, idEvento);
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            throw new CustomException(CustomException.ERRO_EXCLUIR + e.getMessage());
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
